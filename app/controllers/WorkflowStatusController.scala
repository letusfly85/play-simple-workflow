package controllers

import javax.inject._

import entities.{WorkflowStatusEntity}
import models.{WorkflowStatus}
import org.webjars.play.WebJarsUtil
import play.api.libs.json._
import play.api.mvc._
import play.filters.csrf.{CSRFAddToken, CSRFCheck}

@Singleton
class WorkflowStatusController @Inject()(
  cc: ControllerComponents,
  addToken: CSRFAddToken,
  checkToken: CSRFCheck,
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder
) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def list = checkToken(Action { implicit request =>
    val workflowStatuses = WorkflowStatus.findAll().sortBy(w => w.workflowStepId)

    Ok(Json.toJson(workflowStatuses.map{we =>
      WorkflowStatusEntity(
        id = we.id,
        workflowId = we.workflowId.getOrElse(0),
        path = we.path.getOrElse(""),  //TODO
        stepId = we.workflowStepId.getOrElse(0)
      )
    }))
  })
}
