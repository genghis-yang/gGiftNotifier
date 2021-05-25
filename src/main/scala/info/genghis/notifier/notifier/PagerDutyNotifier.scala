package info.genghis.notifier.notifier

import cats.effect.Sync
import cats.implicits._
import info.genghis.notifier.config.AppConfig
import info.genghis.notifier.error.PagerDutyTriggerError
import io.circe.syntax._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.client.Client
import org.http4s.{Method, Request, Uri}

class PagerDutyNotifier[M[_]: Sync](appConfig: AppConfig, client: Client[M]) {
  def notify(summary: String, link: String): M[Unit] = for {
    uri <- Sync[M].fromEither(Uri.fromString(appConfig.pagerDutyUrl))
    body = PagerDutyEvent(appConfig.pagerDutyApiKey, summary, "GiftNotifier", link).asJson
    request = Request[M]()
      .withMethod(Method.POST)
      .withUri(uri)
      .withEntity(body)
    _ <- Sync[M].delay(println(request))
    _ <- Sync[M].delay(println(body))
    _ <- client.status(request).ensure(PagerDutyTriggerError(request.toString, body))(_.isSuccess)
  } yield ()
}
