package bot.line.model

case class Message(
                    id: String,
                    `type`: String,
                    text: String
                  )

case class UserSource(
                       `type`: String,
                       userId: String
                     )

case class LINETextMessageEvent(
                                 replyToken: String,
                                 `type`: String,
                                 timestamp: Long,
                                 source: UserSource,
                                 message: Message
                               )

case class Events(events: List[LINETextMessageEvent])