package entities

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class WorkflowEngineGroupEntity(workflowId: Int, beforeWorkflowId: Int)

object WorkflowEngineGroupEntity {
  implicit def workflowEngineGroupReads: Reads[WorkflowEngineGroupEntity] = (
      (JsPath \ "workflow_id").read[Int] and
      (JsPath \ "before_workflow_id").read[Int]
    )(WorkflowEngineGroupEntity.apply _)

  implicit def workflowEngineGroupWrites: Writes[WorkflowEngineGroupEntity] = (
      (JsPath \ "workflow_id").write[Int] and
      (JsPath \ "before_workflow_id").write[Int]
    )(unlift(WorkflowEngineGroupEntity.unapply))
}
