package bot.application

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, ValidationRejection}
import bot.application.json.EventsJsonSupport

trait BaseBot extends EventsJsonSupport {

  val channelSecret: String

  def verifySignature(channelSecret: String): Directive0 =
    (headerValueByName("X-Line-Signature") & entity(as[String])).tflatMap {
      case (signature, body) if computeSignature(channelSecret, body) == signature => pass
      case _ => reject(ValidationRejection("Invalid signature"))
    }

  private def computeSignature(channelSecret: String, bodyString: String): String = {
    val key = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(key)
    val source = mac.doFinal(bodyString.getBytes(StandardCharsets.UTF_8))
    Base64.getEncoder.encodeToString(source)
  }
}
