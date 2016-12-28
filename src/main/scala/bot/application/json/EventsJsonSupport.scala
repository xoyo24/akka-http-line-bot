package bot.application.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import bot.line.model.event._
import spray.json.DefaultJsonProtocol

trait EventsJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val messageFormat = jsonFormat2(TextMessage)
  implicit val sourceFormat = jsonFormat1(UserSource)
  implicit val textMessageEventFormat = jsonFormat5(LINETextMessageEvent)
  implicit val textMessageEventsFormat = jsonFormat1(Events)
}
