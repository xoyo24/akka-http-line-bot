package bot.line.client

import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.headers.OAuth2BearerToken
import akka.http.scaladsl.model._
import bot.line.json.MessagesJsonSupport
import bot.line.model.send.{Messages, TextMessage}

import scala.concurrent.ExecutionContext

case class ReplyMessageRequest(
                                accessToken: String,
                                replyToken: String,
                                message: String
                              ) extends MessagesJsonSupport {

  def httpRequest(implicit ec: ExecutionContext): HttpRequest = {
    val auth:HttpHeader = headers.Authorization(OAuth2BearerToken(accessToken))
    val content = Messages(
      replyToken = replyToken,
      messages = List(TextMessage(text = message))
    )
    RequestBuilding.Post(
      uri = "https://api.line.me/v2/bot/message/reply",
      content = content
    ).withHeaders(auth)
  }
}
