package controllers

import models.UserCountByProvinceDao
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    //Ok(views.html.index("Your new application is ready."))
    UserCountByProvinceDao.query();
    Ok(views.html.portfolio("JAY"))
  }

}