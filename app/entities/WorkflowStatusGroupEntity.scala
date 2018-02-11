package entities

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class WorkflowStatusGroupEntity(id: Int, workflowId: Int, runningStatus: Int, isCurrent: Boolean)

object WorkflowStatusGroupEntity {
  implicit def workflowEngineReads: Reads[WorkflowStatusGroupEntity] = (
      (JsPath \ "id").read[Int] and
      (JsPath \ "workflow_id").read[Int] and
      (JsPath \ "running_status").read[Int] and
      (JsPath \ "is_current").read[Boolean]
    )(WorkflowStatusGroupEntity.apply _)

  implicit def workflowEngineWrites: Writes[WorkflowStatusGroupEntity] = (
      (JsPath \ "id").write[Int] and
      (JsPath \ "workflow_id").write[Int] and
      (JsPath \ "running_status").write[Int] and
      (JsPath \ "is_current").write[Boolean]
    )(unlift(WorkflowStatusGroupEntity.unapply))
}
