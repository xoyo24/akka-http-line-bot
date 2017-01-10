package bot.application

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Route, ValidationRejection}
import bot.line.client.SignatureVerifier
import bot.line.json.EventsJsonSupport
import bot.line.model.event.{Event, Events}

trait BaseLineBot[T] extends EventsJsonSupport {

  val channelSecret: String
  val signatureVerifier: SignatureVerifier

  def verifySignature: Directive0 =
    (headerValueByName("X-Line-Signature") & entity(as[String])).tflatMap {
      case (signature, body) if signatureVerifier.isValid(channelSecret, body, signature) => pass
      case _ => reject(ValidationRejection("Invalid signature"))
    }

  def routes: Route = {
    (path("line" / "callback") & post & verifySignature & entity(as[Events])) { entity =>
      receive(entity.events)
      complete {
        "OK"
      }
    }
  }

  def receive(events: List[Event]): T
}
