package info.genghis.notifier.notifier

import cats.effect.{IO, Resource}
import info.genghis.notifier.config.AppConfig
import info.genghis.notifier.error.PagerDutyTriggerError
import org.http4s.client.Client
import org.http4s.{Request, Response, Status}
import org.specs2.mutable.Specification

class PagerDutyNotifierSpec extends Specification {

  val appConfig: AppConfig = AppConfig("http://watch.url", "https://pagerduty.url", "PAGERDUTY_API_KEY")

  "PagerDutyNotifierSpec" >> {
    "notify" should {
      "return unit if succeed in notifying" in {
        val client = Client[IO] { _: Request[IO] =>
          Resource.pure[IO, Response[IO]](
            Response[IO]().withStatus(Status.Ok)
          )
        }
        val notifier = new PagerDutyNotifier(appConfig, client)
        notifier.notify("Test Summary", appConfig.watchUrl).unsafeRunSync() must_== (())
      }

      "return unit if failed in notifying" in {
        val client = Client[IO] { _: Request[IO] =>
          Resource.pure[IO, Response[IO]](
            Response[IO]().withStatus(Status.BadRequest)
          )
        }
        val notifier = new PagerDutyNotifier(appConfig, client)
        notifier.notify("Test Summary", appConfig.watchUrl).unsafeRunSync() must throwA[PagerDutyTriggerError]
      }
    }
  }
}
