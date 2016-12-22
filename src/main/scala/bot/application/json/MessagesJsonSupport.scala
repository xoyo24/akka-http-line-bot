package bot.application.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import bot.line.model.{Messages, TextMessage}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait MessagesJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val textMessageFormat: RootJsonFormat[TextMessage] = jsonFormat2(TextMessage)
  implicit val messageFormat: RootJsonFormat[Messages] = jsonFormat2(Messages)
}
