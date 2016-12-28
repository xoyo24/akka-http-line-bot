package bot.line.model.send

trait Message {
  val `type`: String
}

case class TextMessage(text: String) extends Message {
  override val `type`: String = "text"
}