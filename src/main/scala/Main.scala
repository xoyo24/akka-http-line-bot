import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import bot.application.EchoLineBot
import bot.line.client.{ReplyMessageClient, SignatureVerifier}
import com.typesafe.config.ConfigFactory

object Main extends App {
  implicit val system = ActorSystem("LINE-bot")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val config = ConfigFactory.load()

  val bot = new EchoLineBot(
    channelSecret = config.getString("bot.line.channelSecret"),
    signatureVerifier = new SignatureVerifier,
    messageReplier = new ReplyMessageClient(config.getString("bot.line.accessToken"))
  )

  Http().bindAndHandle(bot.routes, config.getString("http.interface"), config.getInt("http.port"))
}
