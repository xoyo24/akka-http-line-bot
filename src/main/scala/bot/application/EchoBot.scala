package bot.application

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Route}
import bot.application.json.EventsJsonSupport
import bot.line.model.Events

class EchoBot(val channelSecret: String, val replyService: ReplyService) extends EventsJsonSupport {

  def verifySignature(channelSecret: String): Directive0 =
    headerValueByName("X-Line-Signature").flatMap { signature =>
      entity(as[String]).flatMap { bodyString =>
        if (computeSignature(channelSecret, bodyString) == signature) pass
        else reject
      }
    }

  private def computeSignature(channelSecret: String, bodyString: String) = {
    val key = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(key)
    val source = mac.doFinal(bodyString.getBytes(StandardCharsets.UTF_8))
    val signatureN = Base64.getEncoder.encodeToString(source)
    signatureN
  }

  def routes: Route = {
    path("line" / "callback") {
      (post & verifySignature(channelSecret) & entity(as[Events])) { request =>
        request.events.map { event =>
          val replyToken = event.replyToken
          val message = event.message.text

          replyService.replyMessage(replyToken, message)
        }
        complete {
          "OK"
        }
      }
    }
  }
}
