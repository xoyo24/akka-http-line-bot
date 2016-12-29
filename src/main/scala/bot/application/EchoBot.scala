package bot.application

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import bot.line.model.{Events, LINETextMessageEvent}

class EchoBot(val channelSecret: String, val replyService: ReplyService) extends BaseBot {

  def routes: Route = {
    (path("line" / "callback") & post & verifySignature(channelSecret) & entity(as[Events])) { body =>
      body.events.collect {
        case LINETextMessageEvent(replyToken, _, _, _, message) =>
          replyService.replyMessage(replyToken, message.text)
      }
      complete {
        "OK"
      }
    }
  }
}
