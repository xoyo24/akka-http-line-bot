package bot.application

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import bot.line.model.event.{Events, MessageEvent, TextMessage}

class EchoBot(val channelSecret: String, val replyService: ReplyService) extends BaseBot {

  def routes: Route = {
    (path("line" / "callback") & post & verifySignature(channelSecret) & entity(as[Events])) { body =>
      body.events.collect {
        case MessageEvent(replyToken, _, _, message) => message match {
          case TextMessage(_, text) =>
            replyService.replyMessage(replyToken, text)
          case _ => ()
        }

      }
      complete {
        "OK"
      }
    }
  }
}
