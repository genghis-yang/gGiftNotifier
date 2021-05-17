package info.genghis.notifier

import cats.effect.Sync
import cats.implicits._
import info.genghis.notifier.config.AppConfig
import info.genghis.notifier.error.{NoDestinationGoodFoundError, WatchingError}
import org.http4s.client.Client
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.jdk.CollectionConverters._

class GiftMonitor[M[_]: Sync](appConfig: AppConfig, client: Client[M]) {
  def watch(): M[Boolean] = for {
    pageHtml <- client.expectOr[String](appConfig.watchUrl)(resp => resp.as[String].map(WatchingError(resp.status, _)))
    goods <- Sync[M].delay(
      Jsoup
        .parse(pageHtml)
        .select("div.exchange_list > ul.goods_list > li.good_list_item")
        .asScala
        .toList
    )
    destGood <- goods.find(good =>
      good.getElementsByClass("goodsName").asScala.headOption.exists(name => name.text.contains("米技微电脑多功能电饭煲EC301"))
        && good.getElementsByTag("a").asScala.headOption.exists(_.attr("href") == "/sm.php/Goods/goods_detail.html?keynum=95A4E88EDCF4400E8FD816F204E8EDE0")
    ).fold(Sync[M].raiseError[Element](NoDestinationGoodFoundError))(Sync[M].pure(_))
    soldOut = destGood.select(".dui").size() > 0
  } yield soldOut
}
