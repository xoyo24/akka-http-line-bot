package bot.line.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import bot.line.model.send.{Message, Messages, TextMessage}
import spray.json._

trait MessagesJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object MessageFormat extends RootJsonFormat[Message] {
    def write(e: Message): JsValue = e match {
      case TextMessage(text) => JsObject(
        "type" -> JsString(e.`type`),
        "text" -> JsString(text)
      )
    }

    def read(value: JsValue): Message = value match {
      case JsArray(Vector(JsString(messageType), JsString(text))) =>
        if (messageType == "text") TextMessage(text)
        else deserializationError(s"Unsupported Message Type '$messageType' !")
      case _ => deserializationError("Message expected")
    }
  }

  implicit val messageFormat: RootJsonFormat[Messages] = jsonFormat2(Messages)
}
