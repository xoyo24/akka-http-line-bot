package bot.application.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import bot.line.model.{Events, LINETextMessageEvent, Message, UserSource}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait EventsJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val messageFormat: RootJsonFormat[Message] = jsonFormat3(Message)
  implicit val sourceFormat: RootJsonFormat[UserSource] = jsonFormat2(UserSource)
  implicit val textMessageEventFormat: RootJsonFormat[LINETextMessageEvent] = jsonFormat5(LINETextMessageEvent)
  implicit val textMessageEventsFormat: RootJsonFormat[Events] = jsonFormat1(Events)
}
