package entities

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class WorkflowEngineEntity(id: Int, workflowId: Int, path: String, stepId: Int, isFirstStep: Boolean, isLastStep: Boolean)

object WorkflowEngineEntity {
  implicit def workflowEngineReads: Reads[WorkflowEngineEntity] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "workflow_id").read[Int] and
      (JsPath \ "path").read[String] and
      (JsPath \ "step_id").read[Int] and
      (JsPath \ "is_first_step").read[Boolean] and
      (JsPath \ "is_last_step").read[Boolean]
    )(WorkflowEngineEntity.apply _)

  implicit def workflowEngineWrites: Writes[WorkflowEngineEntity] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "workflow_id").write[Int] and
      (JsPath \ "path").write[String] and
      (JsPath \ "step_id").write[Int] and
      (JsPath \ "is_first_step").write[Boolean] and
      (JsPath \ "is_last_step").write[Boolean]
    )(unlift(WorkflowEngineEntity.unapply))
}