package controllers

import models.{UserCountByProvince, UserCountByProvinceDao}
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

class UCntByPvcCtrl extends Controller {
  implicit val writes = new Writes[UserCountByProvince] {
    def writes(p: UserCountByProvince) = Json.obj(
      "province" -> p.province,
      "count" -> p.count
    )
  }

  /*def showTable = Action.async {
    val futureRes = Future {
      UserCountByProvinceDao.query()
    }
    futureRes.map(r => Ok(views.html.index(r)))
  }*/
}
