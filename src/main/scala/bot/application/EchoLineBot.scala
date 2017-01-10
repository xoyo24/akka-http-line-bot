package bot.application

import bot.line.client.{MessageReplier, SignatureVerifier}
import bot.line.model.event.{Event, MessageEvent, TextMessage}

class EchoLineBot(
                   val channelSecret: String,
                   val signatureVerifier: SignatureVerifier,
                   val messageReplier: MessageReplier
                 ) extends BaseLineBot[Unit] {

  override def receive(events: List[Event]): Unit = events.collect {
    case MessageEvent(replyToken, _, _, message) => message match {
      case TextMessage(_, text) =>
        messageReplier.replyMessage(replyToken, text)
      case _ => ()
    }

  }

}
