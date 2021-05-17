package info.genghis.notifier

import cats.effect._
import info.genghis.notifier.config.AppConfig
import info.genghis.notifier.error.AppError
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    BlazeClientBuilder[IO](global).resource.use(program _ andThen handleError)

  def program(client: Client[IO]): IO[ExitCode] = for {
    appConfig <- AppConfig.loadConfigFrom[IO](sys.env)
    giftMonitor = new GiftMonitor(appConfig, client)
    pagerDutyNotifier = new PagerDutyNotifier[IO](appConfig, client)
    soldOut <- giftMonitor.watch()
    _ <- IO.whenA(soldOut)(IO(println("Still sold out!")))
    _ <- IO.whenA(!soldOut)(pagerDutyNotifier.notify("Gift is available now!", appConfig.watchUrl))
  } yield ExitCode.Success

  def handleError(x: IO[ExitCode]): IO[ExitCode] = x.handleErrorWith {
    case err: AppError => IO(println(err.getMessage)).map(_ => ExitCode.Error)
    case other         => IO(other.printStackTrace()).map(_ => ExitCode.Error)
  }

}
