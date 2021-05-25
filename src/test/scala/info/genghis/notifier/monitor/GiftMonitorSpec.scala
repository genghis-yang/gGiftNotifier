package info.genghis.notifier.monitor

import cats.effect.{IO, Resource}
import info.genghis.notifier.config.AppConfig
import info.genghis.notifier.error.{NoDestinationGoodFoundError, WatchingError}
import org.http4s.client.Client
import org.http4s.headers.`Content-Type`
import org.http4s.{MediaType, Request, Response, Status}
import org.specs2.mutable.Specification

class GiftMonitorSpec extends Specification {

  val appConfig: AppConfig = AppConfig("http://watch.url", "https://pagerduty.url", "PAGERDUTY_API_KEY")

  "GiftMonitorSpec" >> {
    "watch" should {
      "return true if the response contains destination item and specific class" in {
        val client = Client[IO] { _: Request[IO] =>
          Resource.pure[IO, Response[IO]](
            Response[IO]()
              .withStatus(Status.Ok)
              .withEntity(soldOutHtmlPage)
              .withContentType(`Content-Type`.apply(MediaType.text.html))
          )
        }
        val monitor = new GiftMonitor[IO](appConfig, client)
        monitor.watch().unsafeRunSync() must beTrue
      }

      "return false if the response contains destination item but not the specific class" in {
        val client = Client[IO] { _: Request[IO] =>
          Resource.pure[IO, Response[IO]](
            Response[IO]()
              .withStatus(Status.Ok)
              .withEntity(onSellHtmlPage)
              .withContentType(`Content-Type`.apply(MediaType.text.html))
          )
        }
        val monitor = new GiftMonitor[IO](appConfig, client)
        monitor.watch().unsafeRunSync() must beFalse
      }

      "raise error if the response does not contain destination item" in {
        val client = Client[IO] { _: Request[IO] =>
          Resource.pure[IO, Response[IO]](
            Response[IO]()
              .withStatus(Status.Ok)
              .withEntity(emptyHtmlPage)
              .withContentType(`Content-Type`.apply(MediaType.text.html))
          )
        }
        val monitor = new GiftMonitor[IO](appConfig, client)
        monitor.watch().unsafeRunSync() must throwA(NoDestinationGoodFoundError)
      }

      "raise error if the response status is not successful" in {
        val client = Client[IO] { _: Request[IO] =>
          Resource.pure[IO, Response[IO]](
            Response[IO]()
              .withStatus(Status.NotFound)
              .withEmptyBody
          )
        }
        val monitor = new GiftMonitor[IO](appConfig, client)
        monitor.watch().unsafeRunSync() must throwA(WatchingError(Status.NotFound, ""))
      }
    }
  }

  val soldOutHtmlPage: String = """<!DOCTYPE html>
                   |<html>
                   |<head>
                   |    <meta charset="utf-8">
                   |    <title class="title">ThoughtWorks</title>
                   |    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                   |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                   |    <meta content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=2.0, user-scalable=no, width=device-width" name="viewport">
                   |    <link href="/Public/smtheme1/css/bootstrap.min.css" rel="stylesheet">
                   |    <link href="/Public/smtheme1/css/common.css" rel="stylesheet">
                   |    <link rel="stylesheet" href="/Public/smtheme1/css/index.css">
                   |    <link rel="stylesheet" href="/Public/smtheme1/css/list.css">
                   |    <link rel="stylesheet" href="/Public/smtheme1/css/animate.min.css">
                   |    <link rel="stylesheet" href="/Public/smtheme1/css/swiper.min.css">
                   |    <link rel="stylesheet" href="/Public/smtheme1/css/ejld.css">
                   |    <link rel="stylesheet" href="/Public/smtheme1/mui/css/mui.min.css">
                   |   <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
                   |    <script type="text/javascript">
                   |    if (navigator.appName == 'Microsoft Internet Explorer') {
                   |        if (navigator.userAgent.indexOf("MSIE 5.0") > 0 || navigator.userAgent.indexOf("MSIE 6.0") > 0 || navigator.userAgent.indexOf("MSIE 7.0") > 0) {
                   |            alert('您使用的 IE 浏览器版本过低, 推荐使用 Chrome 浏览器或 IE8 及以上版本浏览器.');
                   |        }
                   |    }
                   |    </script>
                   |
                   |</head>
                   |<style>
                   |.swiper-wrapper{
                   |position:static !important;
                   |}
                   |.swiper-slide{
                   |position:static !important;
                   |}
                   |.swiper-container{
                   |	position:static !important;
                   |}
                   |	.shipin video{
                   |		height: 20rem !important;
                   |	}
                   |	#newBridge .nb-icon-wrap{
                   |		bottom: 36px !important;
                   |	}
                   |	.allgoods{
                   |		height: auto;
                   |	}
                   |</style>
                   |<body style="background: #f2f2f2;">
                   |	    <input type="hidden" value="0" name="is_5" id="is_5">
                   |	    <input type="hidden" value="" name="text_6" id="text_6">
                   |		<input type="hidden" value="6B3454989DD842A3944928086C8C2CA7" name="skeynum" id="skeynum">
                   |	    <input type="hidden" value="5C958F1614B144BE9E261D1B39DA0549" name="keynum" class="keynum">
                   |    <div id="content">
                   |    	        <div class="swiper-container banner_top" style="margin-bottom: 8px;">
                   |            <div class="swiper-wrapper ">
                   |                <div class="swiper-slide  s_video shipin">
                   |                    <img class="WSCSlide_img" src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/202012/15/202012151232368377.jpg" alt="" style="width:100%;">
                   |                </div>
                   |            </div>
                   |            <!-- 如果需要分页器 -->
                   |            <div class="swiper-pagination"></div>
                   |        </div>
                   |        <div class="header" style="overflow: hidden;background-color:;">
                   |            <div class="header_main" style="position: relative;width:100%;background-color:">
                   |                <p class="to_user" style="color:#000">Dear AII</p>
                   |                <div class="theme_content" style="margin-left: 0px;min-height: 50px">
                   |                    <div class="theme_content" style="margin-left: 0px;min-height: 50px">
                   |                        <p style="text-align: center;"><span style="font-size: 14px;color:#000">恰逢吉时，倾吾之心意，赠君以欢喜。</span></p>
                   |                    </div>
                   |                </div>
                   |                <p class="from_user" style="text-align: right;padding-bottom: 15px;color:#000">Dear</p>
                   |            </div>
                   |        </div>        <div class="top_tittle" style="position:relative;top:1rem">
                   |          <span style="padding-left:1.3rem"> 您可以选择1款商品进行兑换</span>
                   |        </div>
                   |        <div class="custom" style="margin-top: 1rem;">
                   |            <div class="allgoods">ThoughtWorks</div>
                   |            <div class="exchange_list" style="overflow: auto">
                   |                <ul style="overflow: auto" class="goods_list">
                   |                    <li class="good_list_item">
                   |                        <a href="/sm.php/Goods/goods_detail.html?keynum=D89F5C6F41E44F05A32A68EAC84A6B1A">
                   |                            <div class="goods_img">
                   |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202008251514599554.jpg">
                   |                            </div>
                   |                        </a>
                   |                                                                        <a href="#">
                   |                        <a href="/sm.php/Order/payorder.html?keynum=D89F5C6F41E44F05A32A68EAC84A6B1A">
                   |                        <div class="shop_intro">
                   |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">现代行车记录仪E79</p>
                   |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                   |                            <div class="people_N" >
                   |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                   | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                   |
                   |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                   |
                   |                            </div>
                   |                        </div>
                   |                        </a>                    </li><li class="good_list_item">
                   |                        <a href="/sm.php/Goods/goods_detail.html?keynum=2271286EBE724EABB341B4433399545D">
                   |                            <div class="goods_img">
                   |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202008141456108687.jpg">
                   |                            </div>
                   |                        </a>
                   |                                                                        <a href="#">
                   |                        <a href="/sm.php/Order/payorder.html?keynum=2271286EBE724EABB341B4433399545D">
                   |                        <div class="shop_intro">
                   |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">罗莱LOVO 塞纳晨光防螨四季被</p>
                   |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                   |                            <div class="people_N" >
                   |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                   | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                   |
                   |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                   |
                   |                            </div>
                   |                        </div>
                   |                        </a>                    </li><li class="good_list_item">
                   |                        <a href="/sm.php/Goods/goods_detail.html?keynum=A39857BC1D4C47009D6CE29811193BC9">
                   |                            <div class="goods_img">
                   |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012151125242151.jpg">
                   |                            </div>
                   |                        </a>
                   |                                                                        <a href="#">
                   |                        <a href="/sm.php/Order/payorder.html?keynum=A39857BC1D4C47009D6CE29811193BC9">
                   |                        <div class="shop_intro">
                   |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">科尔贝洛挂烫机手持式 红色KR-BL012</p>
                   |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                   |                            <div class="people_N" >
                   |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                   | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                   |
                   |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                   |
                   |                            </div>
                   |                        </div>
                   |                        </a>                    </li><li class="good_list_item">
                   |                        <a href="/sm.php/Goods/goods_detail.html?keynum=04560676E5AF45968118EA2AAB6D8D02">
                   |                            <div class="goods_img">
                   |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012171443458860.jpg">
                   |                            </div>
                   |                        </a>
                   |                                                                        <a href="#">
                   |                        <a href="/sm.php/Order/payorder.html?keynum=04560676E5AF45968118EA2AAB6D8D02">
                   |                        <div class="shop_intro">
                   |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">荣事达臻尚多用蒸锅（三层）RG-ZG2801</p>
                   |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                   |                            <div class="people_N" >
                   |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                   | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                   |
                   |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                   |
                   |                            </div>
                   |                        </div>
                   |                        </a>                    </li><li class="good_list_item">
                   |                        <a href="/sm.php/Goods/goods_detail.html?keynum=ED80759750924B0185B053B47F38DEF2">
                   |                            <div class="goods_img">
                   |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012151121050811.jpg">
                   |                            </div>
                   |                        </a>
                   |                                                                        <a href="#">
                   |                        <a href="/sm.php/Order/payorder.html?keynum=ED80759750924B0185B053B47F38DEF2">
                   |                        <div class="shop_intro">
                   |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">荣事达食材净化机RSD-XC10A</p>
                   |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                   |                            <div class="people_N" >
                   |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                   | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                   |
                   |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                   |
                   |                            </div>
                   |                        </div>
                   |                        </a>                    </li><li class="good_list_item">
                   |                        <a href="/sm.php/Goods/goods_detail.html?keynum=6D36FC8699DD4219BC04F5CB7EB26A5D">
                   |                            <div class="goods_img">
                   |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012171452559631.jpg">
                   |                            </div>
                   |                        </a>
                   |                                                                        <a href="#">
                   |                        <a href="/sm.php/Order/payorder.html?keynum=6D36FC8699DD4219BC04F5CB7EB26A5D">
                   |                        <div class="shop_intro">
                   |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">贝立安便捷式吸尘除螨器BJH-CM2201</p>
                   |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                   |                            <div class="people_N" >
                   |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                   | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                   |
                   |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                   |
                   |                            </div>
                   |                        </div>
                   |                        </a>                    </li><li class="good_list_item">
                   |                        <a href="/sm.php/Goods/goods_detail.html?keynum=95A4E88EDCF4400E8FD816F204E8EDE0">
                   |                            <div class="goods_img">
                   |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012171448422313.jpg">
                   |                            </div>
                   |                        </a>
                   |                        <div class="shop_intro">
                   |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">米技微电脑多功能电饭煲EC301</p>
                   |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                   |                            <div class="people_N" >
                   |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                   | 									<p  style="width: 9rem;color: #fff;background-color:gainsboro;text-align: center;" IsDuiHuan="0" IsWebTip="0" IsDuiHuanTip="此商品已断货！" class="dui">立即兑换</p>
                   |
                   |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                   |
                   |                            </div>
                   |                        </div>
                   |
                   |                                            </li><li class="good_list_item">
                   |                        <a href="/sm.php/Goods/goods_detail.html?keynum=1F53B885A4474CE79899087E99AC4A1E">
                   |                            <div class="goods_img">
                   |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/201912051518129585.jpg">
                   |                            </div>
                   |                        </a>
                   |                                                                        <a href="#">
                   |                        <a href="/sm.php/Order/payorder.html?keynum=1F53B885A4474CE79899087E99AC4A1E">
                   |                        <div class="shop_intro">
                   |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">和正脉冲波护眼仪HZ-HYY-1</p>
                   |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                   |                            <div class="people_N" >
                   |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                   | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                   |
                   |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                   |
                   |                            </div>
                   |                        </div>
                   |                        </a>                    </li><li class="good_list_item">
                   |                        <a href="/sm.php/Goods/goods_detail.html?keynum=ED48B8518808483C91B639E978636866">
                   |                            <div class="goods_img">
                   |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012151206360395.jpg">
                   |                            </div>
                   |                        </a>
                   |                                                                        <a href="#">
                   |                        <a href="/sm.php/Order/payorder.html?keynum=ED48B8518808483C91B639E978636866">
                   |                        <div class="shop_intro">
                   |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">荣事达颈部按摩器RSD-AM911S3</p>
                   |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                   |                            <div class="people_N" >
                   |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                   | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                   |
                   |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                   |
                   |                            </div>
                   |                        </div>
                   |                        </a>                    </li><li class="good_list_item">
                   |                        <a href="/sm.php/Goods/goods_detail.html?keynum=B5D053B12BA449B9ADA7A4C8E19CBC63">
                   |                            <div class="goods_img">
                   |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012171502361769.jpg">
                   |                            </div>
                   |                        </a>
                   |                                                                        <a href="#">
                   |                        <a href="/sm.php/Order/payorder.html?keynum=B5D053B12BA449B9ADA7A4C8E19CBC63">
                   |                        <div class="shop_intro">
                   |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">华伦天奴赛瑞斯时尚拉杆箱GV0811XZ 20寸</p>
                   |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                   |                            <div class="people_N" >
                   |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                   | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                   |
                   |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                   |
                   |                            </div>
                   |                        </div>
                   |                        </a>                    </li>                </ul>
                   |            </div>
                   |            <div class="recommended" style="display:block;height: 105px">
                   |            </div>
                   |        </div>
                   |    </div>
                   |
                   |    <div id="output"></div>
                   |   <ul class="tabbar">
                   |   <li class="tabar_item home_page index">
                   |        <a href="/sm.php/Index/index.html">
                   |            <div class="tabbar_icon">
                   |                <img src="/Public/smtheme1/picture/home_p.png" alt="">
                   |            </div>
                   |            <p class="tabbar_label">首页</p>
                   |        </a>
                   |    </li>
                   |
                   |
                   |
                   |
                   |   <li class="tabar_item zz_page gwc1">
                   |        <a href="/sm.php/Index/give_customer.html">
                   |            <div class="tabbar_icon">
                   |                <img src="/Public/smtheme1/picture/zz_p.png" alt="">
                   |            </div>
                   |            <p class="tabbar_label">转赠</p>
                   |        </a>
                   |    </li>
                   |
                   |    <li class="tabar_item shop_page">
                   |        <a href="/sm.php/Goods/cart.html">
                   |            <div class="tabbar_icon">
                   |                <i class="red_icon">0</i>                <img src="/Public/smtheme1/picture/shopcar_p.png" alt="">
                   |            </div>
                   |            <p class="tabbar_label">购物车</p>
                   |        </a>
                   |    </li>
                   |
                   |    <li class="tabar_item order_page">
                   |        <a href="/sm.php/Order/order_login.html">
                   |            <div class="tabbar_icon">
                   |                <img src="/Public/smtheme1/picture/order_p.png" alt="">
                   |            </div>
                   |            <p class="tabbar_label" style="">订单查询</p>
                   |        </a>
                   |    </li>
                   |     </ul>
                   |    <!--include file="public/footer"/-->
                   |
                   |</body>
                   |<script type="text/javascript" src="/Public/smtheme1/js/jquery-1.11.1.min.js"></script>
                   |<script type="text/javascript" src="/Public/smtheme1/mui/js/mui.min.js"></script>
                   |<script type="text/javascript" src="/Public/smtheme1/layer/layer.js"></script>
                   |</html>
                   |<script>
                   |function tips(tips,keynum){
                   |        var resultStr = tips.replace(/\ +/g, ""); //去掉空格
                   |        resultStr = tips.replace(/[ ]/g, "");    //去掉空格
                   |        resultStr = tips.replace(/[\r\n]/g, ""); //去掉回车换行
                   |        //询问框
                   |            var confirm=layer.confirm(resultStr, {
                   |              title:"温馨提示",
                   |              btn: ['确定','取消'] //按钮
                   |            }, function(){
                   |                  window.location.href='/sm.php/Order/payorder.html?keynum='+keynum;
                   |            }, function(){
                   |                  layer.close(confirm);
                   |            });
                   |
                   |        }
                   |	var is_5=$("#is_5").val();
                   |	var text_6=$("#text_6").val();
                   |	if(is_5==1){
                   |		layer.open({
                   |   title: [
                   |    '温馨提示',
                   |  ],
                   |    content: text_6
                   |    ,btn: '我知道了'
                   |  });
                   |	}
                   |
                   |</script>
                   |<script>
                   |	// var skeynum=$('#skeynum').val();
                   |	// if(skeynum=='4C5D0959EE49440AADE7518DBE26BD1E'){
                   |	// 	layer.open({
                   | //  title: [
                   | //   '温馨提示',
                   | // ],
                   | //   content: '<p>简沃本色抽纸从重庆工厂直发，疫情期间物流时效较慢，烦请您耐心等待收货，我们会一直为您催促物流加急配送。</p></p>'
                   | //   ,btn: '我知道了'
                   | // });
                   |	// }
                   |//	alert(skeynum);
                   |</script>
                   |<script type="text/javascript">
                   |$(".home_page").find("img").attr("src", "/Public/smtheme1/picture/handle_home.gif");
                   |//$(".home_page").find("p").css("color", "#000 ");
                   |
                   |
                   |</script>
                   |<script>
                   |$('.dui').click(function(){
                   |	var isdui=$(this).attr('IsDuiHuan');
                   |	var tan=$(this).attr('IsDuiHuanTip');
                   |	if(isdui==0){
                   |	  layer.open({
                   |      title: [
                   |    '温馨提示',
                   |  ],
                   |    content: tan
                   |  });
                   |	}
                   |})
                   |
                   |</script>
                   |<script>
                   |    wx.config({
                   |        debug: false,
                   |        appId: "wx93b3a15deb69791a",
                   |        timestamp: "1621942880",
                   |        nonceStr: "LkIfXZPLkP85p60o",
                   |        signature: "94d80a911878758c5c706b0d5c25139a46ce1f0e",
                   |        jsApiList: [
                   |            'onMenuShareAppMessage',
                   |            'onMenuShareQQ',
                   |            'checkJsApi',
                   |        ]
                   |    });
                   |
                   |    wx.ready(function() {
                   |        wx.onMenuShareAppMessage({
                   |            title: "", // 分享标题
                   |            desc: "祝贺您节日快乐，身体健康", // 分享描述
                   |            link: "", // 分享链接
                   |            imgUrl: "",
                   |           // imgUrl: "", // 分享图标
                   |            success: function() {
                   |                // 用户确认分享后执行的回调函数
                   |                alert("礼品分享成功");
                   |                var keynum=$(".keynum").val();
                   |                var code=$(".code").val();
                   |                if(keynum!=""){
                   |                    window.location.href="/sm.php/Index/index.html?keynum="+keynum;
                   |                }else if(code!=""){
                   |                    window.location.href="/sm.php/Index/index.html?code="+code;
                   |                }
                   |
                   |            },
                   |            cancel: function() {
                   |                // 用户取消分享后执行的回调函数
                   |            }
                   |        });
                   |    });
                   |    </script>
                   |
                   |""".stripMargin

  val onSellHtmlPage: String = """<!DOCTYPE html>
                          |<html>
                          |<head>
                          |    <meta charset="utf-8">
                          |    <title class="title">ThoughtWorks</title>
                          |    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                          |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                          |    <meta content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=2.0, user-scalable=no, width=device-width" name="viewport">
                          |    <link href="/Public/smtheme1/css/bootstrap.min.css" rel="stylesheet">
                          |    <link href="/Public/smtheme1/css/common.css" rel="stylesheet">
                          |    <link rel="stylesheet" href="/Public/smtheme1/css/index.css">
                          |    <link rel="stylesheet" href="/Public/smtheme1/css/list.css">
                          |    <link rel="stylesheet" href="/Public/smtheme1/css/animate.min.css">
                          |    <link rel="stylesheet" href="/Public/smtheme1/css/swiper.min.css">
                          |    <link rel="stylesheet" href="/Public/smtheme1/css/ejld.css">
                          |    <link rel="stylesheet" href="/Public/smtheme1/mui/css/mui.min.css">
                          |   <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
                          |    <script type="text/javascript">
                          |    if (navigator.appName == 'Microsoft Internet Explorer') {
                          |        if (navigator.userAgent.indexOf("MSIE 5.0") > 0 || navigator.userAgent.indexOf("MSIE 6.0") > 0 || navigator.userAgent.indexOf("MSIE 7.0") > 0) {
                          |            alert('您使用的 IE 浏览器版本过低, 推荐使用 Chrome 浏览器或 IE8 及以上版本浏览器.');
                          |        }
                          |    }
                          |    </script>
                          |
                          |</head>
                          |<style>
                          |.swiper-wrapper{
                          |position:static !important;
                          |}
                          |.swiper-slide{
                          |position:static !important;
                          |}
                          |.swiper-container{
                          |	position:static !important;
                          |}
                          |	.shipin video{
                          |		height: 20rem !important;
                          |	}
                          |	#newBridge .nb-icon-wrap{
                          |		bottom: 36px !important;
                          |	}
                          |	.allgoods{
                          |		height: auto;
                          |	}
                          |</style>
                          |<body style="background: #f2f2f2;">
                          |	    <input type="hidden" value="0" name="is_5" id="is_5">
                          |	    <input type="hidden" value="" name="text_6" id="text_6">
                          |		<input type="hidden" value="6B3454989DD842A3944928086C8C2CA7" name="skeynum" id="skeynum">
                          |	    <input type="hidden" value="5C958F1614B144BE9E261D1B39DA0549" name="keynum" class="keynum">
                          |    <div id="content">
                          |    	        <div class="swiper-container banner_top" style="margin-bottom: 8px;">
                          |            <div class="swiper-wrapper ">
                          |                <div class="swiper-slide  s_video shipin">
                          |                    <img class="WSCSlide_img" src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/202012/15/202012151232368377.jpg" alt="" style="width:100%;">
                          |                </div>
                          |            </div>
                          |            <!-- 如果需要分页器 -->
                          |            <div class="swiper-pagination"></div>
                          |        </div>
                          |        <div class="header" style="overflow: hidden;background-color:;">
                          |            <div class="header_main" style="position: relative;width:100%;background-color:">
                          |                <p class="to_user" style="color:#000">Dear AII</p>
                          |                <div class="theme_content" style="margin-left: 0px;min-height: 50px">
                          |                    <div class="theme_content" style="margin-left: 0px;min-height: 50px">
                          |                        <p style="text-align: center;"><span style="font-size: 14px;color:#000">恰逢吉时，倾吾之心意，赠君以欢喜。</span></p>
                          |                    </div>
                          |                </div>
                          |                <p class="from_user" style="text-align: right;padding-bottom: 15px;color:#000">Dear</p>
                          |            </div>
                          |        </div>        <div class="top_tittle" style="position:relative;top:1rem">
                          |          <span style="padding-left:1.3rem"> 您可以选择1款商品进行兑换</span>
                          |        </div>
                          |        <div class="custom" style="margin-top: 1rem;">
                          |            <div class="allgoods">ThoughtWorks</div>
                          |            <div class="exchange_list" style="overflow: auto">
                          |                <ul style="overflow: auto" class="goods_list">
                          |                    <li class="good_list_item">
                          |                        <a href="/sm.php/Goods/goods_detail.html?keynum=D89F5C6F41E44F05A32A68EAC84A6B1A">
                          |                            <div class="goods_img">
                          |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202008251514599554.jpg">
                          |                            </div>
                          |                        </a>
                          |                                                                        <a href="#">
                          |                        <a href="/sm.php/Order/payorder.html?keynum=D89F5C6F41E44F05A32A68EAC84A6B1A">
                          |                        <div class="shop_intro">
                          |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">现代行车记录仪E79</p>
                          |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                          |                            <div class="people_N" >
                          |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                          | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                          |
                          |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                          |
                          |                            </div>
                          |                        </div>
                          |                        </a>                    </li><li class="good_list_item">
                          |                        <a href="/sm.php/Goods/goods_detail.html?keynum=2271286EBE724EABB341B4433399545D">
                          |                            <div class="goods_img">
                          |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202008141456108687.jpg">
                          |                            </div>
                          |                        </a>
                          |                                                                        <a href="#">
                          |                        <a href="/sm.php/Order/payorder.html?keynum=2271286EBE724EABB341B4433399545D">
                          |                        <div class="shop_intro">
                          |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">罗莱LOVO 塞纳晨光防螨四季被</p>
                          |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                          |                            <div class="people_N" >
                          |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                          | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                          |
                          |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                          |
                          |                            </div>
                          |                        </div>
                          |                        </a>                    </li><li class="good_list_item">
                          |                        <a href="/sm.php/Goods/goods_detail.html?keynum=A39857BC1D4C47009D6CE29811193BC9">
                          |                            <div class="goods_img">
                          |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012151125242151.jpg">
                          |                            </div>
                          |                        </a>
                          |                                                                        <a href="#">
                          |                        <a href="/sm.php/Order/payorder.html?keynum=A39857BC1D4C47009D6CE29811193BC9">
                          |                        <div class="shop_intro">
                          |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">科尔贝洛挂烫机手持式 红色KR-BL012</p>
                          |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                          |                            <div class="people_N" >
                          |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                          | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                          |
                          |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                          |
                          |                            </div>
                          |                        </div>
                          |                        </a>                    </li><li class="good_list_item">
                          |                        <a href="/sm.php/Goods/goods_detail.html?keynum=04560676E5AF45968118EA2AAB6D8D02">
                          |                            <div class="goods_img">
                          |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012171443458860.jpg">
                          |                            </div>
                          |                        </a>
                          |                                                                        <a href="#">
                          |                        <a href="/sm.php/Order/payorder.html?keynum=04560676E5AF45968118EA2AAB6D8D02">
                          |                        <div class="shop_intro">
                          |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">荣事达臻尚多用蒸锅（三层）RG-ZG2801</p>
                          |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                          |                            <div class="people_N" >
                          |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                          | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                          |
                          |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                          |
                          |                            </div>
                          |                        </div>
                          |                        </a>                    </li><li class="good_list_item">
                          |                        <a href="/sm.php/Goods/goods_detail.html?keynum=ED80759750924B0185B053B47F38DEF2">
                          |                            <div class="goods_img">
                          |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012151121050811.jpg">
                          |                            </div>
                          |                        </a>
                          |                                                                        <a href="#">
                          |                        <a href="/sm.php/Order/payorder.html?keynum=ED80759750924B0185B053B47F38DEF2">
                          |                        <div class="shop_intro">
                          |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">荣事达食材净化机RSD-XC10A</p>
                          |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                          |                            <div class="people_N" >
                          |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                          | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                          |
                          |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                          |
                          |                            </div>
                          |                        </div>
                          |                        </a>                    </li><li class="good_list_item">
                          |                        <a href="/sm.php/Goods/goods_detail.html?keynum=6D36FC8699DD4219BC04F5CB7EB26A5D">
                          |                            <div class="goods_img">
                          |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012171452559631.jpg">
                          |                            </div>
                          |                        </a>
                          |                                                                        <a href="#">
                          |                        <a href="/sm.php/Order/payorder.html?keynum=6D36FC8699DD4219BC04F5CB7EB26A5D">
                          |                        <div class="shop_intro">
                          |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">贝立安便捷式吸尘除螨器BJH-CM2201</p>
                          |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                          |                            <div class="people_N" >
                          |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                          | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                          |
                          |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                          |
                          |                            </div>
                          |                        </div>
                          |                        </a>                    </li><li class="good_list_item">
                          |                        <a href="/sm.php/Goods/goods_detail.html?keynum=95A4E88EDCF4400E8FD816F204E8EDE0">
                          |                            <div class="goods_img">
                          |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012171448422313.jpg">
                          |                            </div>
                          |                        </a>
                          |                        <div class="shop_intro">
                          |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">米技微电脑多功能电饭煲EC301</p>
                          |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                          |                            <div class="people_N" >
                          |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                          | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                          |
                          |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                          |
                          |                            </div>
                          |                        </div>
                          |
                          |                                            </li><li class="good_list_item">
                          |                        <a href="/sm.php/Goods/goods_detail.html?keynum=1F53B885A4474CE79899087E99AC4A1E">
                          |                            <div class="goods_img">
                          |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/201912051518129585.jpg">
                          |                            </div>
                          |                        </a>
                          |                                                                        <a href="#">
                          |                        <a href="/sm.php/Order/payorder.html?keynum=1F53B885A4474CE79899087E99AC4A1E">
                          |                        <div class="shop_intro">
                          |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">和正脉冲波护眼仪HZ-HYY-1</p>
                          |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                          |                            <div class="people_N" >
                          |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                          | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                          |
                          |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                          |
                          |                            </div>
                          |                        </div>
                          |                        </a>                    </li><li class="good_list_item">
                          |                        <a href="/sm.php/Goods/goods_detail.html?keynum=ED48B8518808483C91B639E978636866">
                          |                            <div class="goods_img">
                          |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012151206360395.jpg">
                          |                            </div>
                          |                        </a>
                          |                                                                        <a href="#">
                          |                        <a href="/sm.php/Order/payorder.html?keynum=ED48B8518808483C91B639E978636866">
                          |                        <div class="shop_intro">
                          |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">荣事达颈部按摩器RSD-AM911S3</p>
                          |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                          |                            <div class="people_N" >
                          |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                          | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                          |
                          |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                          |
                          |                            </div>
                          |                        </div>
                          |                        </a>                    </li><li class="good_list_item">
                          |                        <a href="/sm.php/Goods/goods_detail.html?keynum=B5D053B12BA449B9ADA7A4C8E19CBC63">
                          |                            <div class="goods_img">
                          |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012171502361769.jpg">
                          |                            </div>
                          |                        </a>
                          |                                                                        <a href="#">
                          |                        <a href="/sm.php/Order/payorder.html?keynum=B5D053B12BA449B9ADA7A4C8E19CBC63">
                          |                        <div class="shop_intro">
                          |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">华伦天奴赛瑞斯时尚拉杆箱GV0811XZ 20寸</p>
                          |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                          |                            <div class="people_N" >
                          |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                          | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                          |
                          |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                          |
                          |                            </div>
                          |                        </div>
                          |                        </a>                    </li>                </ul>
                          |            </div>
                          |            <div class="recommended" style="display:block;height: 105px">
                          |            </div>
                          |        </div>
                          |    </div>
                          |
                          |    <div id="output"></div>
                          |   <ul class="tabbar">
                          |   <li class="tabar_item home_page index">
                          |        <a href="/sm.php/Index/index.html">
                          |            <div class="tabbar_icon">
                          |                <img src="/Public/smtheme1/picture/home_p.png" alt="">
                          |            </div>
                          |            <p class="tabbar_label">首页</p>
                          |        </a>
                          |    </li>
                          |
                          |
                          |
                          |
                          |   <li class="tabar_item zz_page gwc1">
                          |        <a href="/sm.php/Index/give_customer.html">
                          |            <div class="tabbar_icon">
                          |                <img src="/Public/smtheme1/picture/zz_p.png" alt="">
                          |            </div>
                          |            <p class="tabbar_label">转赠</p>
                          |        </a>
                          |    </li>
                          |
                          |    <li class="tabar_item shop_page">
                          |        <a href="/sm.php/Goods/cart.html">
                          |            <div class="tabbar_icon">
                          |                <i class="red_icon">0</i>                <img src="/Public/smtheme1/picture/shopcar_p.png" alt="">
                          |            </div>
                          |            <p class="tabbar_label">购物车</p>
                          |        </a>
                          |    </li>
                          |
                          |    <li class="tabar_item order_page">
                          |        <a href="/sm.php/Order/order_login.html">
                          |            <div class="tabbar_icon">
                          |                <img src="/Public/smtheme1/picture/order_p.png" alt="">
                          |            </div>
                          |            <p class="tabbar_label" style="">订单查询</p>
                          |        </a>
                          |    </li>
                          |     </ul>
                          |    <!--include file="public/footer"/-->
                          |
                          |</body>
                          |<script type="text/javascript" src="/Public/smtheme1/js/jquery-1.11.1.min.js"></script>
                          |<script type="text/javascript" src="/Public/smtheme1/mui/js/mui.min.js"></script>
                          |<script type="text/javascript" src="/Public/smtheme1/layer/layer.js"></script>
                          |</html>
                          |<script>
                          |function tips(tips,keynum){
                          |        var resultStr = tips.replace(/\ +/g, ""); //去掉空格
                          |        resultStr = tips.replace(/[ ]/g, "");    //去掉空格
                          |        resultStr = tips.replace(/[\r\n]/g, ""); //去掉回车换行
                          |        //询问框
                          |            var confirm=layer.confirm(resultStr, {
                          |              title:"温馨提示",
                          |              btn: ['确定','取消'] //按钮
                          |            }, function(){
                          |                  window.location.href='/sm.php/Order/payorder.html?keynum='+keynum;
                          |            }, function(){
                          |                  layer.close(confirm);
                          |            });
                          |
                          |        }
                          |	var is_5=$("#is_5").val();
                          |	var text_6=$("#text_6").val();
                          |	if(is_5==1){
                          |		layer.open({
                          |   title: [
                          |    '温馨提示',
                          |  ],
                          |    content: text_6
                          |    ,btn: '我知道了'
                          |  });
                          |	}
                          |
                          |</script>
                          |<script>
                          |	// var skeynum=$('#skeynum').val();
                          |	// if(skeynum=='4C5D0959EE49440AADE7518DBE26BD1E'){
                          |	// 	layer.open({
                          | //  title: [
                          | //   '温馨提示',
                          | // ],
                          | //   content: '<p>简沃本色抽纸从重庆工厂直发，疫情期间物流时效较慢，烦请您耐心等待收货，我们会一直为您催促物流加急配送。</p></p>'
                          | //   ,btn: '我知道了'
                          | // });
                          |	// }
                          |//	alert(skeynum);
                          |</script>
                          |<script type="text/javascript">
                          |$(".home_page").find("img").attr("src", "/Public/smtheme1/picture/handle_home.gif");
                          |//$(".home_page").find("p").css("color", "#000 ");
                          |
                          |
                          |</script>
                          |<script>
                          |$('.dui').click(function(){
                          |	var isdui=$(this).attr('IsDuiHuan');
                          |	var tan=$(this).attr('IsDuiHuanTip');
                          |	if(isdui==0){
                          |	  layer.open({
                          |      title: [
                          |    '温馨提示',
                          |  ],
                          |    content: tan
                          |  });
                          |	}
                          |})
                          |
                          |</script>
                          |<script>
                          |    wx.config({
                          |        debug: false,
                          |        appId: "wx93b3a15deb69791a",
                          |        timestamp: "1621942880",
                          |        nonceStr: "LkIfXZPLkP85p60o",
                          |        signature: "94d80a911878758c5c706b0d5c25139a46ce1f0e",
                          |        jsApiList: [
                          |            'onMenuShareAppMessage',
                          |            'onMenuShareQQ',
                          |            'checkJsApi',
                          |        ]
                          |    });
                          |
                          |    wx.ready(function() {
                          |        wx.onMenuShareAppMessage({
                          |            title: "", // 分享标题
                          |            desc: "祝贺您节日快乐，身体健康", // 分享描述
                          |            link: "", // 分享链接
                          |            imgUrl: "",
                          |           // imgUrl: "", // 分享图标
                          |            success: function() {
                          |                // 用户确认分享后执行的回调函数
                          |                alert("礼品分享成功");
                          |                var keynum=$(".keynum").val();
                          |                var code=$(".code").val();
                          |                if(keynum!=""){
                          |                    window.location.href="/sm.php/Index/index.html?keynum="+keynum;
                          |                }else if(code!=""){
                          |                    window.location.href="/sm.php/Index/index.html?code="+code;
                          |                }
                          |
                          |            },
                          |            cancel: function() {
                          |                // 用户取消分享后执行的回调函数
                          |            }
                          |        });
                          |    });
                          |    </script>
                          |
                          |""".stripMargin

  val emptyHtmlPage: String = """<!DOCTYPE html>
                                |<html>
                                |<head>
                                |    <meta charset="utf-8">
                                |    <title class="title">ThoughtWorks</title>
                                |    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                                |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                |    <meta content="initial-scale=1.0, minimum-scale=1.0, maximum-scale=2.0, user-scalable=no, width=device-width" name="viewport">
                                |    <link href="/Public/smtheme1/css/bootstrap.min.css" rel="stylesheet">
                                |    <link href="/Public/smtheme1/css/common.css" rel="stylesheet">
                                |    <link rel="stylesheet" href="/Public/smtheme1/css/index.css">
                                |    <link rel="stylesheet" href="/Public/smtheme1/css/list.css">
                                |    <link rel="stylesheet" href="/Public/smtheme1/css/animate.min.css">
                                |    <link rel="stylesheet" href="/Public/smtheme1/css/swiper.min.css">
                                |    <link rel="stylesheet" href="/Public/smtheme1/css/ejld.css">
                                |    <link rel="stylesheet" href="/Public/smtheme1/mui/css/mui.min.css">
                                |   <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
                                |    <script type="text/javascript">
                                |    if (navigator.appName == 'Microsoft Internet Explorer') {
                                |        if (navigator.userAgent.indexOf("MSIE 5.0") > 0 || navigator.userAgent.indexOf("MSIE 6.0") > 0 || navigator.userAgent.indexOf("MSIE 7.0") > 0) {
                                |            alert('您使用的 IE 浏览器版本过低, 推荐使用 Chrome 浏览器或 IE8 及以上版本浏览器.');
                                |        }
                                |    }
                                |    </script>
                                |
                                |</head>
                                |<style>
                                |.swiper-wrapper{
                                |position:static !important;
                                |}
                                |.swiper-slide{
                                |position:static !important;
                                |}
                                |.swiper-container{
                                |	position:static !important;
                                |}
                                |	.shipin video{
                                |		height: 20rem !important;
                                |	}
                                |	#newBridge .nb-icon-wrap{
                                |		bottom: 36px !important;
                                |	}
                                |	.allgoods{
                                |		height: auto;
                                |	}
                                |</style>
                                |<body style="background: #f2f2f2;">
                                |	    <input type="hidden" value="0" name="is_5" id="is_5">
                                |	    <input type="hidden" value="" name="text_6" id="text_6">
                                |		<input type="hidden" value="6B3454989DD842A3944928086C8C2CA7" name="skeynum" id="skeynum">
                                |	    <input type="hidden" value="5C958F1614B144BE9E261D1B39DA0549" name="keynum" class="keynum">
                                |    <div id="content">
                                |    	        <div class="swiper-container banner_top" style="margin-bottom: 8px;">
                                |            <div class="swiper-wrapper ">
                                |                <div class="swiper-slide  s_video shipin">
                                |                    <img class="WSCSlide_img" src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/202012/15/202012151232368377.jpg" alt="" style="width:100%;">
                                |                </div>
                                |            </div>
                                |            <!-- 如果需要分页器 -->
                                |            <div class="swiper-pagination"></div>
                                |        </div>
                                |        <div class="header" style="overflow: hidden;background-color:;">
                                |            <div class="header_main" style="position: relative;width:100%;background-color:">
                                |                <p class="to_user" style="color:#000">Dear AII</p>
                                |                <div class="theme_content" style="margin-left: 0px;min-height: 50px">
                                |                    <div class="theme_content" style="margin-left: 0px;min-height: 50px">
                                |                        <p style="text-align: center;"><span style="font-size: 14px;color:#000">恰逢吉时，倾吾之心意，赠君以欢喜。</span></p>
                                |                    </div>
                                |                </div>
                                |                <p class="from_user" style="text-align: right;padding-bottom: 15px;color:#000">Dear</p>
                                |            </div>
                                |        </div>        <div class="top_tittle" style="position:relative;top:1rem">
                                |          <span style="padding-left:1.3rem"> 您可以选择1款商品进行兑换</span>
                                |        </div>
                                |        <div class="custom" style="margin-top: 1rem;">
                                |            <div class="allgoods">ThoughtWorks</div>
                                |            <div class="exchange_list" style="overflow: auto">
                                |                <ul style="overflow: auto" class="goods_list">
                                |                    <li class="good_list_item">
                                |                        <a href="/sm.php/Goods/goods_detail.html?keynum=D89F5C6F41E44F05A32A68EAC84A6B1A">
                                |                            <div class="goods_img">
                                |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202008251514599554.jpg">
                                |                            </div>
                                |                        </a>
                                |                                                                        <a href="#">
                                |                        <a href="/sm.php/Order/payorder.html?keynum=D89F5C6F41E44F05A32A68EAC84A6B1A">
                                |                        <div class="shop_intro">
                                |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">现代行车记录仪E79</p>
                                |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                                |                            <div class="people_N" >
                                |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                                | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                                |
                                |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                                |
                                |                            </div>
                                |                        </div>
                                |                        </a>                    </li><li class="good_list_item">
                                |                        <a href="/sm.php/Goods/goods_detail.html?keynum=2271286EBE724EABB341B4433399545D">
                                |                            <div class="goods_img">
                                |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202008141456108687.jpg">
                                |                            </div>
                                |                        </a>
                                |                                                                        <a href="#">
                                |                        <a href="/sm.php/Order/payorder.html?keynum=2271286EBE724EABB341B4433399545D">
                                |                        <div class="shop_intro">
                                |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">罗莱LOVO 塞纳晨光防螨四季被</p>
                                |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                                |                            <div class="people_N" >
                                |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                                | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                                |
                                |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                                |
                                |                            </div>
                                |                        </div>
                                |                        </a>                    </li><li class="good_list_item">
                                |                        <a href="/sm.php/Goods/goods_detail.html?keynum=A39857BC1D4C47009D6CE29811193BC9">
                                |                            <div class="goods_img">
                                |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012151125242151.jpg">
                                |                            </div>
                                |                        </a>
                                |                                                                        <a href="#">
                                |                        <a href="/sm.php/Order/payorder.html?keynum=A39857BC1D4C47009D6CE29811193BC9">
                                |                        <div class="shop_intro">
                                |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">科尔贝洛挂烫机手持式 红色KR-BL012</p>
                                |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                                |                            <div class="people_N" >
                                |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                                | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                                |
                                |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                                |
                                |                            </div>
                                |                        </div>
                                |                        </a>                    </li><li class="good_list_item">
                                |                        <a href="/sm.php/Goods/goods_detail.html?keynum=04560676E5AF45968118EA2AAB6D8D02">
                                |                            <div class="goods_img">
                                |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012171443458860.jpg">
                                |                            </div>
                                |                        </a>
                                |                                                                        <a href="#">
                                |                        <a href="/sm.php/Order/payorder.html?keynum=04560676E5AF45968118EA2AAB6D8D02">
                                |                        <div class="shop_intro">
                                |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">荣事达臻尚多用蒸锅（三层）RG-ZG2801</p>
                                |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                                |                            <div class="people_N" >
                                |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                                | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                                |
                                |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                                |
                                |                            </div>
                                |                        </div>
                                |                        </a>                    </li><li class="good_list_item">
                                |                        <a href="/sm.php/Goods/goods_detail.html?keynum=ED80759750924B0185B053B47F38DEF2">
                                |                            <div class="goods_img">
                                |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012151121050811.jpg">
                                |                            </div>
                                |                        </a>
                                |                                                                        <a href="#">
                                |                        <a href="/sm.php/Order/payorder.html?keynum=ED80759750924B0185B053B47F38DEF2">
                                |                        <div class="shop_intro">
                                |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">荣事达食材净化机RSD-XC10A</p>
                                |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                                |                            <div class="people_N" >
                                |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                                | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                                |
                                |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                                |
                                |                            </div>
                                |                        </div>
                                |                        </a>                    </li><li class="good_list_item">
                                |                        <a href="/sm.php/Goods/goods_detail.html?keynum=6D36FC8699DD4219BC04F5CB7EB26A5D">
                                |                            <div class="goods_img">
                                |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012171452559631.jpg">
                                |                            </div>
                                |                        </a>
                                |                                                                        <a href="#">
                                |                        <a href="/sm.php/Order/payorder.html?keynum=6D36FC8699DD4219BC04F5CB7EB26A5D">
                                |                        <div class="shop_intro">
                                |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">贝立安便捷式吸尘除螨器BJH-CM2201</p>
                                |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                                |                            <div class="people_N" >
                                |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                                | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                                |
                                |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                                |
                                |                            </div>
                                |                        </div>
                                |                        </a>                    </li><li class="good_list_item">
                                |                        <a href="/sm.php/Goods/goods_detail.html?keynum=1F53B885A4474CE79899087E99AC4A1E">
                                |                            <div class="goods_img">
                                |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/201912051518129585.jpg">
                                |                            </div>
                                |                        </a>
                                |                                                                        <a href="#">
                                |                        <a href="/sm.php/Order/payorder.html?keynum=1F53B885A4474CE79899087E99AC4A1E">
                                |                        <div class="shop_intro">
                                |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">和正脉冲波护眼仪HZ-HYY-1</p>
                                |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                                |                            <div class="people_N" >
                                |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                                | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                                |
                                |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                                |
                                |                            </div>
                                |                        </div>
                                |                        </a>                    </li><li class="good_list_item">
                                |                        <a href="/sm.php/Goods/goods_detail.html?keynum=ED48B8518808483C91B639E978636866">
                                |                            <div class="goods_img">
                                |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012151206360395.jpg">
                                |                            </div>
                                |                        </a>
                                |                                                                        <a href="#">
                                |                        <a href="/sm.php/Order/payorder.html?keynum=ED48B8518808483C91B639E978636866">
                                |                        <div class="shop_intro">
                                |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">荣事达颈部按摩器RSD-AM911S3</p>
                                |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                                |                            <div class="people_N" >
                                |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                                | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                                |
                                |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                                |
                                |                            </div>
                                |                        </div>
                                |                        </a>                    </li><li class="good_list_item">
                                |                        <a href="/sm.php/Goods/goods_detail.html?keynum=B5D053B12BA449B9ADA7A4C8E19CBC63">
                                |                            <div class="goods_img">
                                |                                <img src="http://kaceduoxuanyi.oss-cn-beijing.aliyuncs.com/zly1905061347424742051/UpFiles/good/202012171502361769.jpg">
                                |                            </div>
                                |                        </a>
                                |                                                                        <a href="#">
                                |                        <a href="/sm.php/Order/payorder.html?keynum=B5D053B12BA449B9ADA7A4C8E19CBC63">
                                |                        <div class="shop_intro">
                                |                            <p class="goodsName" style="margin-bottom: 3px;min-height:3px;">华伦天奴赛瑞斯时尚拉杆箱GV0811XZ 20寸</p>
                                |                            <p class="" style="font-size: 12px;">★点击图片查看套餐商品详情</p>
                                |                            <div class="people_N" >
                                |                                <p class="people_p">  <!--<span>0</span>人兑换--></p>
                                | 									<p  style="width: 9rem;color: #fff;background-color:red;text-align: center;"   >立即兑换</p>
                                |
                                |                                <!--<img src="/Public/smtheme1/picture/add_img.png" alt="" class="add_img">-->
                                |
                                |                            </div>
                                |                        </div>
                                |                        </a>                    </li>                </ul>
                                |            </div>
                                |            <div class="recommended" style="display:block;height: 105px">
                                |            </div>
                                |        </div>
                                |    </div>
                                |
                                |    <div id="output"></div>
                                |   <ul class="tabbar">
                                |   <li class="tabar_item home_page index">
                                |        <a href="/sm.php/Index/index.html">
                                |            <div class="tabbar_icon">
                                |                <img src="/Public/smtheme1/picture/home_p.png" alt="">
                                |            </div>
                                |            <p class="tabbar_label">首页</p>
                                |        </a>
                                |    </li>
                                |
                                |
                                |
                                |
                                |   <li class="tabar_item zz_page gwc1">
                                |        <a href="/sm.php/Index/give_customer.html">
                                |            <div class="tabbar_icon">
                                |                <img src="/Public/smtheme1/picture/zz_p.png" alt="">
                                |            </div>
                                |            <p class="tabbar_label">转赠</p>
                                |        </a>
                                |    </li>
                                |
                                |    <li class="tabar_item shop_page">
                                |        <a href="/sm.php/Goods/cart.html">
                                |            <div class="tabbar_icon">
                                |                <i class="red_icon">0</i>                <img src="/Public/smtheme1/picture/shopcar_p.png" alt="">
                                |            </div>
                                |            <p class="tabbar_label">购物车</p>
                                |        </a>
                                |    </li>
                                |
                                |    <li class="tabar_item order_page">
                                |        <a href="/sm.php/Order/order_login.html">
                                |            <div class="tabbar_icon">
                                |                <img src="/Public/smtheme1/picture/order_p.png" alt="">
                                |            </div>
                                |            <p class="tabbar_label" style="">订单查询</p>
                                |        </a>
                                |    </li>
                                |     </ul>
                                |    <!--include file="public/footer"/-->
                                |
                                |</body>
                                |<script type="text/javascript" src="/Public/smtheme1/js/jquery-1.11.1.min.js"></script>
                                |<script type="text/javascript" src="/Public/smtheme1/mui/js/mui.min.js"></script>
                                |<script type="text/javascript" src="/Public/smtheme1/layer/layer.js"></script>
                                |</html>
                                |<script>
                                |function tips(tips,keynum){
                                |        var resultStr = tips.replace(/\ +/g, ""); //去掉空格
                                |        resultStr = tips.replace(/[ ]/g, "");    //去掉空格
                                |        resultStr = tips.replace(/[\r\n]/g, ""); //去掉回车换行
                                |        //询问框
                                |            var confirm=layer.confirm(resultStr, {
                                |              title:"温馨提示",
                                |              btn: ['确定','取消'] //按钮
                                |            }, function(){
                                |                  window.location.href='/sm.php/Order/payorder.html?keynum='+keynum;
                                |            }, function(){
                                |                  layer.close(confirm);
                                |            });
                                |
                                |        }
                                |	var is_5=$("#is_5").val();
                                |	var text_6=$("#text_6").val();
                                |	if(is_5==1){
                                |		layer.open({
                                |   title: [
                                |    '温馨提示',
                                |  ],
                                |    content: text_6
                                |    ,btn: '我知道了'
                                |  });
                                |	}
                                |
                                |</script>
                                |<script>
                                |	// var skeynum=$('#skeynum').val();
                                |	// if(skeynum=='4C5D0959EE49440AADE7518DBE26BD1E'){
                                |	// 	layer.open({
                                | //  title: [
                                | //   '温馨提示',
                                | // ],
                                | //   content: '<p>简沃本色抽纸从重庆工厂直发，疫情期间物流时效较慢，烦请您耐心等待收货，我们会一直为您催促物流加急配送。</p></p>'
                                | //   ,btn: '我知道了'
                                | // });
                                |	// }
                                |//	alert(skeynum);
                                |</script>
                                |<script type="text/javascript">
                                |$(".home_page").find("img").attr("src", "/Public/smtheme1/picture/handle_home.gif");
                                |//$(".home_page").find("p").css("color", "#000 ");
                                |
                                |
                                |</script>
                                |<script>
                                |$('.dui').click(function(){
                                |	var isdui=$(this).attr('IsDuiHuan');
                                |	var tan=$(this).attr('IsDuiHuanTip');
                                |	if(isdui==0){
                                |	  layer.open({
                                |      title: [
                                |    '温馨提示',
                                |  ],
                                |    content: tan
                                |  });
                                |	}
                                |})
                                |
                                |</script>
                                |<script>
                                |    wx.config({
                                |        debug: false,
                                |        appId: "wx93b3a15deb69791a",
                                |        timestamp: "1621946713",
                                |        nonceStr: "olfXYvux66niVKaq",
                                |        signature: "9a8b473bc950937a72203ad6f3acfc5c8dcb3d73",
                                |        jsApiList: [
                                |            'onMenuShareAppMessage',
                                |            'onMenuShareQQ',
                                |            'checkJsApi',
                                |        ]
                                |    });
                                |
                                |    wx.ready(function() {
                                |        wx.onMenuShareAppMessage({
                                |            title: "", // 分享标题
                                |            desc: "祝贺您节日快乐，身体健康", // 分享描述
                                |            link: "", // 分享链接
                                |            imgUrl: "",
                                |           // imgUrl: "", // 分享图标
                                |            success: function() {
                                |                // 用户确认分享后执行的回调函数
                                |                alert("礼品分享成功");
                                |                var keynum=$(".keynum").val();
                                |                var code=$(".code").val();
                                |                if(keynum!=""){
                                |                    window.location.href="/sm.php/Index/index.html?keynum="+keynum;
                                |                }else if(code!=""){
                                |                    window.location.href="/sm.php/Index/index.html?code="+code;
                                |                }
                                |
                                |            },
                                |            cancel: function() {
                                |                // 用户取消分享后执行的回调函数
                                |            }
                                |        });
                                |    });
                                |    </script>
                                |
                                |""".stripMargin
}
