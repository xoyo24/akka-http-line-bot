package bot.application

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.testkit.ScalatestRouteTest
import bot.line.client.SignatureVerifier
import bot.line.json.EventsJsonSupport
import bot.line.model.event._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, _}

class BaseLineBotSpec
  extends FlatSpec
    with Matchers
    with ScalatestRouteTest
    with EventsJsonSupport
    with MockFactory {

  def createBot(
                 sv: SignatureVerifier = mock[SignatureVerifier],
                 rv:List[Event] => Unit
               ): BaseLineBot[Unit] = new BaseLineBot[Unit] {
    override val channelSecret: String = "channelSecret"
    override val signatureVerifier: SignatureVerifier = sv

    override def receive(events: List[Event]): Unit = rv(events)
  }

  it should "Verify signature" in {
    val signatureVerifier = stub[SignatureVerifier]
    (signatureVerifier.isValid _).when(*, *, *) returns true
    val receive = stubFunction[List[Event], Unit]
    receive.when(*).returns(Unit)
    val bot = createBot(
      signatureVerifier,
      receive
    )
    val event = MessageEvent(
      replyToken = "replyToken",
      timestamp = 0,
      source = UserSource(id = "1"),
      message = TextMessage(id = "2", text = "test message")
    )
    val body = Events(List(event))
    val header = RawHeader("X-Line-Signature", "signature")

    Post("/line/callback", body).withHeaders(header) ~> bot.routes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe "OK"
    }
    (signatureVerifier.isValid _).verify("channelSecret", *, "signature").once
    receive.verify(body.events).once
  }

}
