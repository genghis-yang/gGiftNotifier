package info.genghis.notifier.config

import cats.effect.Sync
import cats.implicits._
import info.genghis.notifier.error.MissingApplicationConfigError

case class AppConfig(
    watchUrl: String,
    pagerDutyUrl: String,
    pagerDutyApiKey: String
)

object AppConfig {
  def loadConfigFrom[M[_]: Sync](env: Map[String, String]): M[AppConfig] =
    Sync[M].fromOption(
      (
        env.get("WATCH_URL"),
        env.get("PAGER_DUTY_URL"),
        env.get("PAGER_DUTY_API_KEY")
      ).mapN(AppConfig(_, _, _)),
      MissingApplicationConfigError
    )
}
