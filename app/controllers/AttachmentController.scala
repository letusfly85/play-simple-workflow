package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class AttachmentController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def show(itemId: String) = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def create(itemId: String) = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
