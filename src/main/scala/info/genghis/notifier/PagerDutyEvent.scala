package info.genghis.notifier

import io.circe.syntax._
import io.circe.{Encoder, Json}

sealed trait EventSeverity

case object Critical extends EventSeverity
case object Warning extends EventSeverity
case object Error extends EventSeverity
case object Info extends EventSeverity

object EventSeverity {
  implicit val encoder: Encoder[EventSeverity] = severity =>
    (severity match {
      case Critical => "critical"
      case Warning  => "warning"
      case Error    => "error"
      case Info     => "info"
    }).asJson
}

sealed trait EventAction

case object Trigger extends EventAction
case object Acknowledge extends EventAction
case object Resolve extends EventAction

object EventAction {
  implicit val encoder: Encoder[EventAction] = action =>
    (action match {
      case Trigger     => "trigger"
      case Acknowledge => "acknowledge"
      case Resolve     => "resolve"
    }).asJson
}

case class PagerDutyEvent(
    pagerDutyApiKey: String,
    summary: String,
    module: String,
    link: String,
    severity: EventSeverity = Info,
    action: EventAction = Trigger,
    imageSrc: String =
      "https://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/202012/15/202012151232368377.jpg"
)

object PagerDutyEvent {
  implicit val encoder: Encoder[PagerDutyEvent] = event =>
    Json.obj(
      "routing_key" -> event.pagerDutyApiKey.asJson,
      "event_action" -> event.action.asJson,
      "payload" -> Json.obj(
        "summary" -> event.summary.asJson,
        "severity" -> event.severity.asJson,
        "source" -> event.module.asJson
      ),
      "links" -> Json.arr(Json.obj("href" -> event.link.asJson)),
      "images" -> Json.arr(Json.obj("src" -> event.imageSrc.asJson, "href" -> event.link.asJson))
    )
}
