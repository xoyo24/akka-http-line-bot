package bot.application.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import bot.line.model.event.{MessageEvent, UserSource, _}
import spray.json.{RootJsonFormat, _}

trait EventsJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object MessageFormat extends RootJsonFormat[Message] {
    def write(e: Message): JsValue = e.toJson

    def read(value: JsValue): Message = value match {
      case JsArray(Vector(JsString(id), JsString(messageType), JsString(text))) =>
        if (messageType == "text") TextMessage(id, text)
        else deserializationError(s"Unsupported Message Type '$messageType' !")
      case _ => deserializationError("Message expected")
    }
  }

  implicit object SourceFormat extends RootJsonFormat[Source] {
    def write(e: Source): JsValue = e.toJson

    def read(value: JsValue): Source = value match {
      case JsArray(Vector(JsString(sourceType), JsString(id))) =>
        if (sourceType == "user") UserSource(id)
        else if (sourceType == "group") GroupSource(id)
        else if (sourceType == "room") RoomSource(id)
        else deserializationError(s"Unsupported Source Type '$sourceType' !")
      case _ => deserializationError("Source expected")
    }
  }

  implicit object EventFormat extends RootJsonFormat[Event] {
    def write(e: Event): JsValue = e.toJson

    def read(value: JsValue): Event = value match {
      case JsArray(Vector(JsString(replyToken), JsString(eventType), JsNumber(timestamp), source, message)) =>
        if (eventType == "message")
          MessageEvent(replyToken, timestamp.toLong, SourceFormat.read(source), MessageFormat.read(message))
        else deserializationError(s"Unsupported Event Type '$eventType' !")
      case _ => deserializationError("Event expected")
    }
  }

  implicit val textMessageEventsFormat = jsonFormat1(Events)
}
