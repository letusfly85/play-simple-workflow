package controllers

import javax.inject._

import org.webjars.play.WebJarsUtil
import play.api.libs.json.JsObject
import play.api.mvc._
import play.filters.csrf.{CSRFAddToken, CSRFCheck}

@Singleton
class ItemController @Inject()(
    cc: ControllerComponents,
    addToken: CSRFAddToken,
    checkToken: CSRFCheck,
    webJarsUtil: WebJarsUtil,
    assets: AssetsFinder
  ) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index = Action { implicit request =>
    Ok(views.html.items(""))
  }

  def list = Action {
    Ok(JsObject.empty)
  }

  def show(id: String) = Action {
    Ok(JsObject.empty)
  }
}
