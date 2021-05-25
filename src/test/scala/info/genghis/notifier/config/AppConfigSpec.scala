package info.genghis.notifier.config

import cats.effect.IO
import info.genghis.notifier.error.MissingApplicationConfigError
import org.specs2.mutable.Specification

class AppConfigSpec extends Specification {

  "AppConfigSpec" >> {
    "loadConfigFrom" should {
      "generate AppConfig if all environment variables are valid" in {
        val env = Map(
          "WATCH_URL" -> "http://watch.url",
          "PAGER_DUTY_URL" -> "https://pagerduty.utl",
          "PAGER_DUTY_API_KEY" -> "PAGERDUTY_API_KEY"
        )
        AppConfig.loadConfigFrom[IO](env).unsafeRunSync() must_== AppConfig(
          "http://watch.url",
          "https://pagerduty.utl",
          "PAGERDUTY_API_KEY"
        )
      }

      "raise error if some environment variables are missing" in {
        val env = Map(
          "WATCH_URL" -> "http://watch.url"
        )
        AppConfig.loadConfigFrom[IO](env).unsafeRunSync() must throwA(MissingApplicationConfigError)
      }
    }
  }
}
