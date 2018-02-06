package entities

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class WorkflowRoutingEntity(userId: Int, workflowId: Int, stepId: Int)

object WorkflowRoutingEntity {
  implicit def workflowRoutingReads: Reads[WorkflowRoutingEntity] = (
      (JsPath \ "user_id").read[Int] and
      (JsPath \ "workflow_id").read[Int] and
      (JsPath \ "workflow_step_id").read[Int]
    )(WorkflowRoutingEntity.apply _)

  implicit def workflowRoutingWrites: Writes[WorkflowRoutingEntity] = (
      (JsPath \ "user_id").write[Int] and
      (JsPath \ "workflow_id").write[Int] and
      (JsPath \ "workflow_step_id").write[Int]
    )(unlift(WorkflowRoutingEntity.unapply))
}
