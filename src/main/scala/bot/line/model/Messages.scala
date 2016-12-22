package bot.line.model

case class TextMessage(`type`: String = "text", text: String)

case class Messages(replyToken: String, messages: List[TextMessage])
