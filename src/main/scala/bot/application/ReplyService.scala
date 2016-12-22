package bot.application

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers._
import akka.stream.Materializer
import bot.line.model.{Messages, TextMessage}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.Success


class ReplyService(accessToken: String)
                  (implicit val system: ActorSystem,
                   implicit val materializer: Materializer,
                   implicit val executionContext: ExecutionContextExecutor
                  ) {

  def replyMessage(token: String, message: String): Future[HttpResponse] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request(token, message))

    responseFuture.andThen {
      case Success(response) if response.status.isSuccess() => println(s"request succeed")
      case error => println(s"request failed: $error")
    }
  }

  private def request(token: String, text: String): HttpRequest = {
    val auth = headers.Authorization(OAuth2BearerToken(accessToken))
    val message = Messages(
      replyToken = token,
      messages = List(TextMessage(text = text))
    )

    RequestBuilding.Post(
      uri = "https://api.line.me/v2/bot/message/reply",
      content = message
    ).withHeaders(auth)
  }
}
