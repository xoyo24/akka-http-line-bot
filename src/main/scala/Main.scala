import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import bot.application.{EchoBot, ReplyService}
import com.typesafe.config.ConfigFactory

object Main extends App {
  implicit val system = ActorSystem("LINE-bot")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val config = ConfigFactory.load()

  val bot = new EchoBot(
    channelSecret = config.getString("bot.line.channelSecret"),
    replyService = new ReplyService(config.getString("bot.line.accessToken"))
  )

  Http().bindAndHandle(bot.routes, config.getString("http.interface"), config.getInt("http.port"))
}
