package entities

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class WorkflowStatusGroupEntity(id: Int, workflowId: Int, runningStatus: Int)

object WorkflowStatusGroupEntity {
  implicit def workflowEngineReads: Reads[WorkflowStatusGroupEntity] = (
      (JsPath \ "id").read[Int] and
      (JsPath \ "workflow_id").read[Int] and
      (JsPath \ "running_status").read[Int]
    )(WorkflowStatusGroupEntity.apply _)

  implicit def workflowEngineWrites: Writes[WorkflowStatusGroupEntity] = (
      (JsPath \ "id").write[Int] and
      (JsPath \ "workflow_id").write[Int] and
      (JsPath \ "running_status").write[Int]
    )(unlift(WorkflowStatusGroupEntity.unapply))
}
