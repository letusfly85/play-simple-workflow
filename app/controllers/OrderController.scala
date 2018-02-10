package controllers

import javax.inject._

import org.webjars.play.WebJarsUtil
import play.api.Logger
import play.api.libs.json.JsObject
import play.api.mvc._
import play.filters.csrf.{CSRFAddToken, CSRFCheck}

@Singleton
class OrderController @Inject()(
  cc: ControllerComponents,
  addToken: CSRFAddToken,
  checkToken: CSRFCheck,
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder
  ) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def list = addToken(Action {
    Ok(JsObject.empty)
  })

  def show(id: String) = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def create = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def confirm = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def review(id: String) = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}
