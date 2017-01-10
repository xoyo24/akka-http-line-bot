package bot.line.model.event

sealed trait Event {
  val `type`: String
  val timestamp: Long
  val source: Source
}

case class MessageEvent(
                         replyToken: String,
                         timestamp: Long,
                         source: Source,
                         message: Message
                       ) extends Event {
  override val `type`: String = "message"
}
