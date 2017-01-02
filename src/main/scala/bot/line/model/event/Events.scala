package bot.line.model.event

case class Events(events: List[Event])

//case class LINETextMessageEvent(
//                                 replyToken: String,
//                                 `type`: String,
//                                 timestamp: Long,
//                                 source: UserSource,
//                                 message: TextMessage
//                               )