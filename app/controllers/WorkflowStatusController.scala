package controllers

import javax.inject._

import entities.{WorkflowStatusEntity, WorkflowStatusGroupEntity}
import models.{WorkflowEngine, WorkflowEngineGroup, WorkflowStatus, WorkflowStatusGroup}
import org.webjars.play.WebJarsUtil
import play.api.libs.json._
import play.api.mvc._
import play.filters.csrf.{CSRFAddToken, CSRFCheck}
import scalikejdbc._

@Singleton
class WorkflowStatusController @Inject()(
  cc: ControllerComponents,
  addToken: CSRFAddToken,
  checkToken: CSRFCheck,
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder
) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  val ws = WorkflowStatus.syntax("ws")
  val we = WorkflowEngine.syntax("we")

  val wsg = WorkflowStatusGroup.syntax("wsg")
  val weg = WorkflowEngineGroup.syntax("weg")

  def list(workflowId: String) = checkToken(Action { implicit request =>
    val statusList =
      DB localTx { implicit session =>
        withSQL {
          select
            .from(WorkflowStatus as ws)
            .innerJoin(WorkflowEngine as we)
            .on(ws.workflowId, we.workflowId)
            .where.eq(ws.workflowStepId, we.workflowStepId)
            .and.eq(ws.workflowId, workflowId)
        }.map(res => (WorkflowStatus(ws)(res), WorkflowEngine(we)(res))).list.apply
      }

    Ok(Json.toJson(
      statusList.map { case (ws, we) =>
        WorkflowStatusEntity(
          id = ws.id,
          workflowId = ws.workflowId.getOrElse(0),
          path = we.path.getOrElse(""),
          stepId = ws.workflowStepId.getOrElse(0),
          isExecuted = ws.isExecuted
        )
      }
    ))
  })

  def groupList = checkToken(Action { implicit request =>
    val statusList =
      DB localTx { implicit session =>
        withSQL {
          select
            .from(WorkflowStatusGroup as wsg)
            .innerJoin(WorkflowEngineGroup as weg)
            .on(wsg.workflowId, weg.workflowId)
        }.map(res => (WorkflowStatusGroup(wsg)(res), WorkflowEngineGroup(weg)(res))).list.apply
      }

    Ok(Json.toJson(
      statusList.map { case (wsg, weg) =>
        WorkflowStatusGroupEntity(
          id = wsg.id,
          workflowId = wsg.workflowId,
          runningStatus = wsg.runningStatus
        )
      }
    ))
  })
}
