package info.genghis.notifier.error

import io.circe.Json
import org.http4s.Status

sealed trait AppError extends Throwable

object AppError {
  implicit class AppErrorOps(appError: AppError) {
    def getMessage(): String = appError.toString
    override def toString: String = appError match {
      case PagerDutyTriggerError(request, body)       => s"Cannot trigger PagerDuty $request with body: $body"
      case WatchingError(errStatus, errPayload) => s"Cannot watch destination web page, got $errStatus, $errPayload"
      case MissingApplicationConfigError        => "Missing required environment variables"
      case NoDestinationGoodFoundError          => "Cannot find the destination good"
    }
  }
}

case class PagerDutyTriggerError(request: String, body: Json) extends AppError
case class WatchingError(errStatus: Status, errPayload: String) extends AppError
case object MissingApplicationConfigError extends AppError
case object NoDestinationGoodFoundError extends AppError
