package bot.line.client

import scala.concurrent.Future

trait MessageReplier {
  def replyMessage(token: String, message: String): Future[Unit]
}
