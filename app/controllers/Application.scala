package controllers

import models.UserCountByProvinceDao
import play.api.mvc._

import scala.concurrent.Future

class Application extends Controller {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def index = Action.async {
    val futureRes = Future {
      UserCountByProvinceDao.query()
    }
    futureRes.map(r => Ok(views.html.portfolio("JAY")))
  }

}