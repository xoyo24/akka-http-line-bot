package bot.application

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers._
import akka.stream.Materializer
import bot.application.json.MessagesJsonSupport
import bot.line.model.{Messages, TextMessage}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.Success


class ReplyService(accessToken: String)
                  (implicit val system: ActorSystem,
                   implicit val materializer: Materializer,
                   implicit val executionContext: ExecutionContextExecutor
                  ) extends MessagesJsonSupport {

  def replyMessage(token: String, message: String): Future[HttpResponse] = {
    val auth = headers.Authorization(OAuth2BearerToken(accessToken))
    val content = Messages(
      replyToken = token,
      messages = List(TextMessage(text = message))
    )

    val request = RequestBuilding.Post(
      uri = "https://api.line.me/v2/bot/message/reply",
      content = content
    ).withHeaders(auth)

    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)

    responseFuture.andThen {
      case Success(response) if response.status.isSuccess() => println(s"message sent!")
      case error => println(s"request failed: $error")
    }
  }

}
