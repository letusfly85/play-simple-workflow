package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class OrderController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

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
