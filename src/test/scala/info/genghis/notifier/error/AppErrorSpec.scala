package info.genghis.notifier.error

import cats.implicits._
import io.circe.Json
import org.http4s.Status
import org.specs2.mutable.Specification

class AppErrorSpec extends Specification {
  "AppErrorSpec" >> {
    "AppError" should {
      "show error message" in {
        val pagerDutyTriggerError: AppError = PagerDutyTriggerError("Test", Json.obj())
        pagerDutyTriggerError.show must_== "Cannot trigger PagerDuty Test with body: {\n  \n}"

        val watchingError: AppError = WatchingError(Status.NotFound, "")
        watchingError.show must_== "Cannot watch destination web page, got 404 Not Found, "

        val missingApplicationConfigError: AppError = MissingApplicationConfigError
        missingApplicationConfigError.show must_== "Missing required environment variables"

        val noDestinationGoodFoundError: AppError = NoDestinationGoodFoundError
        noDestinationGoodFoundError.show must_== "Cannot find the destination good"
      }
    }
  }
}
