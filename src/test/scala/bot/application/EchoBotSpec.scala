package bot.application

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.testkit.ScalatestRouteTest
import bot.line.client.{MessageReplier, SignatureVerifier}
import bot.line.json.EventsJsonSupport
import bot.line.model.event._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, _}

import scala.concurrent.Future

class EchoBotSpec
  extends FlatSpec
    with Matchers
    with ScalatestRouteTest
    with EventsJsonSupport
    with MockFactory {

  def createBot(
                 sv: SignatureVerifier = mock[SignatureVerifier],
                 mr: MessageReplier = mock[MessageReplier]
               ): EchoLineBot = new EchoLineBot(
    channelSecret = "channelSecret",
    signatureVerifier = sv,
    messageReplier = mr
  )

  it should "reply text message as reveived" in {
    val signatureVerifier = stub[SignatureVerifier]
    (signatureVerifier.isValid _).when(*, *, *) returns true
    val messageReplier = stub[MessageReplier]
    (messageReplier.replyMessage _).when(*, *).returns(Future.successful(Unit))

    val bot = createBot(
      signatureVerifier,
      messageReplier
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
    (messageReplier.replyMessage _).verify("replyToken", "test message").once
  }

}
