package bot.line.model.event

case class Events(events: List[LINETextMessageEvent])

case class LINETextMessageEvent(
                                 replyToken: String,
                                 `type`: String,
                                 timestamp: Long,
                                 source: UserSource,
                                 message: TextMessage
                               )