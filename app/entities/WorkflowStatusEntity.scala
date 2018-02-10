package entities

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class WorkflowStatusEntity(id: Int, workflowId: Int, path: String, stepId: Int)

object WorkflowEngineEntity {
  implicit def workflowEngineReads: Reads[WorkflowStatusEntity] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "workflow_id").read[Int] and
      (JsPath \ "path").read[String] and
      (JsPath \ "step_id").read[Int]
    )(WorkflowStatusEntity.apply _)

  implicit def workflowEngineWrites: Writes[WorkflowStatusEntity] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "workflow_id").write[Int] and
      (JsPath \ "path").write[String] and
      (JsPath \ "step_id").write[Int]
    )(unlift(WorkflowStatusEntity.unapply))
}
