package controllers

import javax.inject._

import entities.WorkflowEngineEntity
import play.api.mvc._
import models.WorkflowEngine
import org.webjars.play.WebJarsUtil
import play.api.Logger
import play.api.libs.json._
import play.filters.csrf.{CSRFAddToken, CSRFCheck}

@Singleton
class WorkflowEngineController @Inject()(
  cc: ControllerComponents,
  addToken: CSRFAddToken,
  checkToken: CSRFCheck,
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder
) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index = addToken(Action { implicit request =>
    Ok(views.html.workflowEngine("Your new application is ready."))
  })

  def list = checkToken(Action { implicit request =>
    val workflowEngines = WorkflowEngine.findAll().sortBy(w => w.workflowStepId)

    Ok(Json.toJson(workflowEngines.map{we =>
      WorkflowEngineEntity(
        id = we.id,
        workflowId = we.workflowId.getOrElse(0),
        path = we.path.getOrElse(""),
        method = we.method.getOrElse("GET"),
        stepId = we.workflowStepId.getOrElse(0),
        isFirstStep = we.isFirstStep.getOrElse(false),
        isLastStep = we.isLastStep.getOrElse(false)
      )
    }))
  })

  def create = checkToken(Action { implicit request =>
    request.body.asJson match {
      case Some(json) =>
        Json.fromJson[WorkflowEngineEntity](json) match {
          case JsSuccess(we, _) =>
            WorkflowEngine.create(
              workflowId = Some(we.id),
              path = Some(we.path),
              method = Some(we.method),
              workflowStepId = Some(we.stepId),
              workflowStepNextId = Some(0),
              workflowDescription = Some("description"),
              workflowName = Some("dummy name"),
              isFirstStep = Some(we.isFirstStep),
              isLastStep = Some(we.isLastStep),
              createdAt = new org.joda.time.DateTime,
              updatedAt = new org.joda.time.DateTime
            ).save()

            Ok(views.html.workflowEngine("Your new application is ready."))

          case JsError(e) =>
            Logger.info(s"error ${e.toString()}")
            BadRequest(Json.obj("error_message" -> JsError.toJson(e).toString()))
        }

      case None =>
        Logger.info("no json found")
        BadRequest(Json.obj("error_message" -> "not found json"))
    }
  })

  def update = checkToken(Action { implicit request =>
    request.body.asJson match {
      case Some(json) =>
        Json.fromJson[WorkflowEngineEntity](json) match {
          case JsSuccess(we, _) =>
            WorkflowEngine.find(we.id) match {
              case Some(engine) =>
                engine.copy(
                  path = Some(we.path),
                  method = Some("POST"),
                  workflowStepId = Some(we.stepId),
                  workflowStepNextId = Some(0),
                  isFirstStep = Some(we.isFirstStep),
                  isLastStep = Some(we.isLastStep),
                  updatedAt = new org.joda.time.DateTime
                ).save()

              case None =>
                Logger.info(s"not found  for ${we.id.toString()}")
            }

            val workflowEngines = WorkflowEngine.findAll()
            Ok(Json.toJson(workflowEngines.map{we =>
              WorkflowEngineEntity(
                id = we.id,
                workflowId = we.workflowId.getOrElse(0),
                path = we.path.getOrElse(""),
                method = we.method.getOrElse("GET"),
                stepId = we.workflowStepId.getOrElse(0),
                isFirstStep = we.isFirstStep.getOrElse(false),
                isLastStep = we.isLastStep.getOrElse(false)
              )
            }))

          case JsError(e) =>
            Logger.info(s"error ${e.toString()}")
            BadRequest(Json.obj("error_message" -> JsError.toJson(e).toString()))
        }

      case None =>
        Logger.info("no json found")
        BadRequest(Json.obj("error_message" -> "not found json"))
    }
  })

  def destroy(id: String) = checkToken(Action { implicit request =>
    WorkflowEngine.find(id.toInt) match {
      case Some(engine) =>
        Logger.info(s"deleting ${engine.id} ${engine.path}")
        engine.destroy()

      case None =>
        Logger.info(s"not found  for ${id}")
    }

    val workflowEngines = WorkflowEngine.findAll()
    Ok(Json.toJson(workflowEngines.map{we =>
      WorkflowEngineEntity(
        id = we.id,
        workflowId = we.workflowId.getOrElse(0),
        path = we.path.getOrElse(""),
        method = we.method.getOrElse("GET"),
        stepId = we.workflowStepId.getOrElse(0),
        isFirstStep = we.isFirstStep.getOrElse(false),
        isLastStep = we.isLastStep.getOrElse(false)
      )
    }))
  })

}
