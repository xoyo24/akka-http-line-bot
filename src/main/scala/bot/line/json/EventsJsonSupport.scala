package bot.line.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import bot.line.model.event.{MessageEvent, UserSource, _}
import spray.json.{JsString, RootJsonFormat, _}

trait EventsJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object MessageFormat extends RootJsonFormat[Message] {
    def write(m: Message): JsValue = m match {
      case TextMessage(id, text) => JsObject(
        "id" -> JsString(id),
        "type" -> JsString(m.`type`),
        "text" -> JsString(text)
      )
      case _ => serializationError(s"Unsupported Message Type '${m.`type`}' !")
    }

    def read(value: JsValue): Message = value.asJsObject.getFields("type") match {
      case Seq(JsString("text")) =>
        value.asJsObject.getFields("id", "text") match {
          case Seq(JsString(id), JsString(text)) => TextMessage(id, text)
        }
      case _ => deserializationError(s"Message expected ${value.prettyPrint}")
    }
  }

  implicit object SourceFormat extends RootJsonFormat[Source] {
    def write(s: Source): JsValue = s match {
      case UserSource(id) => JsObject(
        "type" -> JsString(s.`type`),
        "userId" -> JsString(id)
      )
      case GroupSource(id) => JsObject(
        "type" -> JsString(s.`type`),
        "groupId" -> JsString(id)
      )
      case RoomSource(id) => JsObject(
        "type" -> JsString(s.`type`),
        "roomId" -> JsString(id)
      )
      case _ => serializationError(s"Unsupported Source Type '${s.`type`}' !")
    }

    def read(value: JsValue): Source =
      value.asJsObject.getFields("type", "userId", "groupId", "roomId") match {
        case Seq(JsString("user"), JsString(id)) => UserSource(id)
        case Seq(JsString("group"), JsString(id)) => GroupSource(id)
        case Seq(JsString("room"), JsString(id)) => RoomSource(id)
        case _ => deserializationError(s"Source expected")
      }
  }

  implicit object EventFormat extends RootJsonFormat[Event] {
    def write(e: Event): JsValue = e match {
      case MessageEvent(replyToken, timestamp, source, message) => JsObject(
        "replyToken" -> JsString(replyToken),
        "type" -> JsString(e.`type`),
        "timestamp" -> JsNumber(timestamp),
        "source" -> SourceFormat.write(source),
        "message" -> MessageFormat.write(message)
      )
      case _ => serializationError(s"Unsupported Event Type '${e.`type`}' !")
    }

    def read(value: JsValue): Event = {
      value.asJsObject.getFields("replyToken", "type", "timestamp", "source", "message") match {
        case Seq(JsString(replyToken), JsString(eventType), JsNumber(timestamp), source, message) =>
          if (eventType == "message")
            MessageEvent(replyToken, timestamp.toLong, SourceFormat.read(source), MessageFormat.read(message))
          else deserializationError(s"Unsupported Event Type '$eventType' !")
        case _ => deserializationError("Event expected")
      }
    }
  }

  implicit val EventsFormat: RootJsonFormat[Events] = jsonFormat1(Events)
}
