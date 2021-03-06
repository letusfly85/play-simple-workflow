package controllers

import javax.inject._

import entities.{WorkflowEngineEntity, WorkflowEngineGroupEntity, WorkflowRoutingEntity}
import play.api.mvc._
import models.{WorkflowEngine, WorkflowEngineGroup, WorkflowStatus, WorkflowStatusGroup}
import org.webjars.play.WebJarsUtil
import play.api.Logger
import play.api.libs.json._
import play.filters.csrf.{CSRFAddToken, CSRFCheck}
import scalikejdbc._

@Singleton
class WorkflowEngineController @Inject()(
  cc: ControllerComponents,
  addToken: CSRFAddToken,
  checkToken: CSRFCheck,
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder
) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def init = addToken(Action {implicit request =>
    val maybeWorkflowId = request.getQueryString("workflow-id")
    maybeWorkflowId match {
      case Some(workflowId) =>
        val maybeEngine = WorkflowEngine.findBy(
          sqls.eq(WorkflowEngine.column.workflowId, workflowId)
            .and.eq(WorkflowEngine.column.workflowStepId, 1))

        val maybeGroup = WorkflowStatusGroup.findBy(
          sqls.eq(WorkflowStatusGroup.column.workflowId, workflowId)
        )
        maybeGroup match {
          case Some(group) =>
            group.copy(
              isCurrent = Some(true),
              updatedAt = new org.joda.time.DateTime
            ).save()
          case None =>
        }
        WorkflowStatusGroup.findAllBy(
          sqls.ne(WorkflowStatusGroup.column.workflowId, workflowId)
        ).foreach{group =>
          group.copy(
            isCurrent = Some(false),
            updatedAt = new org.joda.time.DateTime
          ).save()
        }

        maybeEngine match {
          case Some(engine) =>
            Logger.info(engine.toString)

            Ok(Json.toJson(
              WorkflowEngineEntity(
                id = engine.id,
                workflowId = workflowId.toInt,
                stepId = engine.workflowStepId.getOrElse(0),
                path = engine.path.getOrElse("/workflow-engines"),
                isFirstStep = engine.isFirstStep.getOrElse(false),
                isLastStep = engine.isLastStep.getOrElse(false)
              )
            ))

          case None =>
            Redirect("/workflow-engines")
        }

      case None =>
        Redirect("/workflow-engines")
    }
  })

  def routes = checkToken(Action {implicit request =>
    request.body.asJson match {
      case Some(json) =>
        Json.fromJson[WorkflowRoutingEntity](json) match {
          case JsSuccess(wr, _) =>
            val maybeStatus = WorkflowStatus.findBy(
              sqls.eq(WorkflowStatus.column.workflowId, wr.workflowId)
                .and.eq(WorkflowStatus.column.workflowStepId, wr.stepId)
            )
            maybeStatus match {
              case Some(status) =>
                Logger.info(status.toString)
                status.copy(
                  isExecuted = true,
                  updatedAt = new org.joda.time.DateTime
                ).save()

              case None =>
            }

            val statuses =
              WorkflowStatus.findAllBy(
                sqls.eq(WorkflowStatus.column.userId, wr.userId)
                    .and.eq(WorkflowStatus.column.workflowId, wr.workflowId)
                  .and.ge(WorkflowStatus.column.workflowStepId, wr.stepId))
            Logger.info(statuses.toString)

            val nextStepId =
              statuses.filter(s => !s.isExecuted).nonEmpty match {
                case true =>
                  statuses.filter(s => !s.isExecuted)
                  .sortBy(s => s.workflowStepId)
                  .head.workflowStepId.getOrElse(1)
                case false => 1
              }

            if (statuses.nonEmpty && statuses.forall(s => s.isExecuted)) {
              WorkflowStatusGroup.findBy(sqls.eq(WorkflowStatusGroup.column.workflowId, wr.workflowId)) match {
                case Some(group) =>
                  group.copy(
                    runningStatus = 1,
                    updatedAt = new org.joda.time.DateTime
                  ).save()
                case None =>
              }
            }

            val maybeEngine = WorkflowEngine.findBy(
              sqls.eq(WorkflowEngine.column.workflowStepId, nextStepId)
                .and.eq(WorkflowEngine.column.workflowId, wr.workflowId)
            )
            maybeEngine match {
              case Some(engine) =>
                val path: String = engine.path.getOrElse("/workflow-engines")
                Logger.info(s"${path}, ${nextStepId.toString}")

                Ok(Json.toJson(
                  WorkflowEngineEntity(
                    id = engine.id,
                    workflowId = engine.workflowId.getOrElse(0),
                    stepId = engine.workflowStepId.getOrElse(0),
                    path = path,
                    isFirstStep = engine.isFirstStep.getOrElse(false),
                    isLastStep = engine.isLastStep.getOrElse(false)
                  )
                ))

              case None =>
                Redirect("/workflow-engines")
            }

          case JsError(e) =>
            Redirect("/workflow-engines")
        }

      case None =>
        Redirect("/workflow-engines")
    }

  })

  def findBelong(workflowId: String) = checkToken(Action { implicit request =>
    val maybeUserId = request.queryString.get("user_id")
    maybeUserId match {
      case Some(userId) =>
        val statuses =
          WorkflowStatus.findAllBy(
            sqls.eq(WorkflowStatus.column.userId, userId.head)
              .and.eq(WorkflowStatus.column.workflowId, workflowId)
          ).sortBy(s => s.workflowStepId)
        if (statuses.nonEmpty) {
          val notExecutedStatus = statuses.filter(s => s.isExecuted == false).sortBy(s => s.workflowStepId)

          if (notExecutedStatus.nonEmpty) {
            Ok(Json.toJson(WorkflowRoutingEntity(
              notExecutedStatus.head.userId,
              notExecutedStatus.head.workflowId.getOrElse(0),
              notExecutedStatus.head.workflowStepId.getOrElse(0))
            ))
          } else {
            Ok(Json.toJson(WorkflowRoutingEntity(
              statuses.head.userId, statuses.head.workflowId.getOrElse(0), statuses.head.workflowStepId.getOrElse(0))
            ))
          }
        } else {
          Ok(JsObject.empty)
        }

      case None =>
        Ok(JsObject.empty)
    }
  })

  def list = checkToken(Action { implicit request =>
    val workflowEngines = WorkflowEngine.findAll().sortBy(w => (w.workflowId, w.workflowStepId))

    Ok(Json.toJson(workflowEngines.map{we =>
      WorkflowEngineEntity(
        id = we.id,
        workflowId = we.workflowId.getOrElse(0),
        path = we.path.getOrElse(""),
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
              workflowId = Some(we.workflowId),
              path = Some(we.path),
              workflowStepId = Some(we.stepId),
              workflowStepNextId = Some(0),
              workflowDescription = Some("description"),
              workflowName = Some("dummy name"),
              isFirstStep = Some(we.isFirstStep),
              isLastStep = Some(we.isLastStep),
              createdAt = new org.joda.time.DateTime,
              updatedAt = new org.joda.time.DateTime
            ).save()

            WorkflowStatus.create(
              workflowId = Some(we.workflowId),
              workflowStepId = Some(we.stepId),
              userId = 0, //todo get from session
              isExecuted = false,
              createdAt = new org.joda.time.DateTime,
              updatedAt = new org.joda.time.DateTime
            )

            val maybeEngineGroup = WorkflowEngineGroup.findBy(sqls.eq(WorkflowEngineGroup.column.workflowId, we.workflowId))
            maybeEngineGroup match {
              case Some(_) =>
              case None =>
                WorkflowEngineGroup.create(
                  workflowGroupId = 0,
                  workflowId = we.workflowId,
                  beforeWorkflowId = None,
                  conditionToStart = None,
                  createdAt = new org.joda.time.DateTime,
                  updatedAt = new org.joda.time.DateTime
                )
            }

            val maybeStatusGroup = WorkflowStatusGroup.findBy(sqls.eq(WorkflowEngineGroup.column.workflowId, we.workflowId))
            maybeStatusGroup match {
              case Some(_) =>
              case None =>
                WorkflowStatusGroup.create(
                  userId = 0,
                  workflowGroupId = 0,
                  workflowId = we.workflowId,
                  runningStatus = 0,
                  createdAt = new org.joda.time.DateTime,
                  updatedAt = new org.joda.time.DateTime
                )
            }

            Ok(JsObject.empty)

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

  def updateGroupDependency = checkToken(Action {implicit request =>
    request.body.asJson match {
      case Some(json) =>
        Json.fromJson[WorkflowEngineGroupEntity](json) match {
          case JsSuccess(weg, _) =>
            WorkflowEngineGroup.findBy(sqls.eq(WorkflowEngineGroup.column.workflowId, weg.workflowId)) match {
              case Some(group) =>
                group.copy(
                  beforeWorkflowId = Some(weg.beforeWorkflowId),
                  updatedAt = new org.joda.time.DateTime()
                ).save()

              case None =>
            }
            Ok(JsObject.empty)

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
        if (WorkflowEngine.findAllBy(sqls.eq(WorkflowEngine.column.workflowId, engine.workflowId)).length == 1) {
          WorkflowEngineGroup.findAllBy(sqls.eq(WorkflowEngineGroup.column.workflowId, engine.workflowId))
            .foreach { group =>
              group.destroy()
            }
          WorkflowStatusGroup.findAllBy(sqls.eq(WorkflowStatusGroup.column.workflowId, engine.workflowId))
            .foreach { group =>
              group.destroy()
            }
        }

        WorkflowStatus.findAllBy(
          sqls.eq(WorkflowStatus.column.workflowId, engine.workflowId)
           .and.eq(WorkflowStatus.column.workflowStepId, engine.workflowStepId)
        ).foreach(ws => ws.destroy())
        engine.destroy()

      case None =>
        Logger.info(s"not found  for ${id}")
    }

    Ok(JsObject.empty)
  })

  def destroyAll(workflowId: String) = checkToken(Action { implicit request =>
    WorkflowEngine.findAllBy(sqls.eq(WorkflowEngine.column.workflowId, workflowId)).foreach { engine =>
        Logger.info(s"deleting ${engine.id} ${engine.path}")

        WorkflowStatus.findAllBy(
          sqls.eq(WorkflowStatus.column.workflowId, engine.workflowId)
            .and.eq(WorkflowStatus.column.workflowStepId, engine.workflowStepId)
        ).foreach(ws => ws.destroy())
        engine.destroy()
    }
    WorkflowEngineGroup.findAllBy(sqls.eq(WorkflowEngineGroup.column.workflowId, workflowId)).foreach(g => g.destroy)
    WorkflowStatusGroup.findAllBy(sqls.eq(WorkflowStatusGroup.column.workflowId, workflowId)).foreach(g => g.destroy)

    Ok(JsObject.empty)
  })
}
