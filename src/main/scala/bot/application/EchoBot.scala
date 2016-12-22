package bot.application

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import bot.application.json.EventsJsonSupport
import bot.line.model.Events

class EchoBot(val replyService: ReplyService) extends EventsJsonSupport {

  def routes: Route = {
    path("line" / "callback") {
      (post & entity(as[Events])) { events =>
        events.events.map { event =>
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