package bot.line.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer

import scala.concurrent.{ExecutionContext, Future}

class ReplyMessageClient(accessToken: String)
                        (implicit val system: ActorSystem,
                         implicit val materializer: Materializer,
                         implicit val ec: ExecutionContext) extends MessageReplier {

  override def replyMessage(replyToken: String, message: String): Future[Unit] = {
    val request = ReplyMessageRequest(accessToken, replyToken, message).httpRequest
    val responseFuture = Http().singleRequest(request)

    responseFuture.collect {
      case response if response.status.isSuccess() => println(s"message sent!")
      case error => println(s"request failed: $error")
    }
  }

}
