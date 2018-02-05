package models

import scalikejdbc._
import org.joda.time.{DateTime}

case class WorkflowEngine(
  id: Int,
  workflowId: Option[Int] = None,
  workflowName: Option[String] = None,
  workflowDescription: Option[String] = None,
  path: Option[String] = None,
  method: Option[String] = None,
  enableRole: Option[String] = None,
  workflowStepId: Option[Int] = None,
  workflowStepNextId: Option[Int] = None,
  conditionForNextStep: Option[String] = None,
  isFirstStep: Option[Boolean] = None,
  isLastStep: Option[Boolean] = None,
  createdAt: DateTime,
  updatedAt: DateTime) {

  def save()(implicit session: DBSession = WorkflowEngine.autoSession): WorkflowEngine = WorkflowEngine.save(this)(session)

  def destroy()(implicit session: DBSession = WorkflowEngine.autoSession): Int = WorkflowEngine.destroy(this)(session)

}


object WorkflowEngine extends SQLSyntaxSupport[WorkflowEngine] {

  override val schemaName = Some("simple_workflow")

  override val tableName = "workflow_engine"

  override val columns = Seq("id", "workflow_id", "workflow_name", "workflow_description", "path", "method", "enable_role", "workflow_step_id", "workflow_step_next_id", "condition_for_next_step", "is_first_step", "is_last_step", "created_at", "updated_at")

  def apply(we: SyntaxProvider[WorkflowEngine])(rs: WrappedResultSet): WorkflowEngine = apply(we.resultName)(rs)
  def apply(we: ResultName[WorkflowEngine])(rs: WrappedResultSet): WorkflowEngine = new WorkflowEngine(
    id = rs.get(we.id),
    workflowId = rs.get(we.workflowId),
    workflowName = rs.get(we.workflowName),
    workflowDescription = rs.get(we.workflowDescription),
    path = rs.get(we.path),
    method = rs.get(we.method),
    enableRole = rs.get(we.enableRole),
    workflowStepId = rs.get(we.workflowStepId),
    workflowStepNextId = rs.get(we.workflowStepNextId),
    conditionForNextStep = rs.get(we.conditionForNextStep),
    isFirstStep = rs.get(we.isFirstStep),
    isLastStep = rs.get(we.isLastStep),
    createdAt = rs.get(we.createdAt),
    updatedAt = rs.get(we.updatedAt)
  )

  val we = WorkflowEngine.syntax("we")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[WorkflowEngine] = {
    withSQL {
      select.from(WorkflowEngine as we).where.eq(we.id, id)
    }.map(WorkflowEngine(we.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[WorkflowEngine] = {
    withSQL(select.from(WorkflowEngine as we)).map(WorkflowEngine(we.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(WorkflowEngine as we)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[WorkflowEngine] = {
    withSQL {
      select.from(WorkflowEngine as we).where.append(where)
    }.map(WorkflowEngine(we.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[WorkflowEngine] = {
    withSQL {
      select.from(WorkflowEngine as we).where.append(where)
    }.map(WorkflowEngine(we.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(WorkflowEngine as we).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    workflowId: Option[Int] = None,
    workflowName: Option[String] = None,
    workflowDescription: Option[String] = None,
    path: Option[String] = None,
    method: Option[String] = None,
    enableRole: Option[String] = None,
    workflowStepId: Option[Int] = None,
    workflowStepNextId: Option[Int] = None,
    conditionForNextStep: Option[String] = None,
    isFirstStep: Option[Boolean] = None,
    isLastStep: Option[Boolean] = None,
    createdAt: DateTime,
    updatedAt: DateTime)(implicit session: DBSession = autoSession): WorkflowEngine = {
    val generatedKey = withSQL {
      insert.into(WorkflowEngine).namedValues(
        column.workflowId -> workflowId,
        column.workflowName -> workflowName,
        column.workflowDescription -> workflowDescription,
        column.path -> path,
        column.method -> method,
        column.enableRole -> enableRole,
        column.workflowStepId -> workflowStepId,
        column.workflowStepNextId -> workflowStepNextId,
        column.conditionForNextStep -> conditionForNextStep,
        column.isFirstStep -> isFirstStep,
        column.isLastStep -> isLastStep,
        column.createdAt -> createdAt,
        column.updatedAt -> updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    WorkflowEngine(
      id = generatedKey.toInt,
      workflowId = workflowId,
      workflowName = workflowName,
      workflowDescription = workflowDescription,
      path = path,
      method = method,
      enableRole = enableRole,
      workflowStepId = workflowStepId,
      workflowStepNextId = workflowStepNextId,
      conditionForNextStep = conditionForNextStep,
      isFirstStep = isFirstStep,
      isLastStep = isLastStep,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: Seq[WorkflowEngine])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'workflowId -> entity.workflowId,
        'workflowName -> entity.workflowName,
        'workflowDescription -> entity.workflowDescription,
        'path -> entity.path,
        'method -> entity.method,
        'enableRole -> entity.enableRole,
        'workflowStepId -> entity.workflowStepId,
        'workflowStepNextId -> entity.workflowStepNextId,
        'conditionForNextStep -> entity.conditionForNextStep,
        'isFirstStep -> entity.isFirstStep,
        'isLastStep -> entity.isLastStep,
        'createdAt -> entity.createdAt,
        'updatedAt -> entity.updatedAt))
    SQL("""insert into workflow_engine(
      workflow_id,
      workflow_name,
      workflow_description,
      path,
      method,
      enable_role,
      workflow_step_id,
      workflow_step_next_id,
      condition_for_next_step,
      is_first_step,
      is_last_step,
      created_at,
      updated_at
    ) values (
      {workflowId},
      {workflowName},
      {workflowDescription},
      {path},
      {method},
      {enableRole},
      {workflowStepId},
      {workflowStepNextId},
      {conditionForNextStep},
      {isFirstStep},
      {isLastStep},
      {createdAt},
      {updatedAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: WorkflowEngine)(implicit session: DBSession = autoSession): WorkflowEngine = {
    withSQL {
      update(WorkflowEngine).set(
        column.id -> entity.id,
        column.workflowId -> entity.workflowId,
        column.workflowName -> entity.workflowName,
        column.workflowDescription -> entity.workflowDescription,
        column.path -> entity.path,
        column.method -> entity.method,
        column.enableRole -> entity.enableRole,
        column.workflowStepId -> entity.workflowStepId,
        column.workflowStepNextId -> entity.workflowStepNextId,
        column.conditionForNextStep -> entity.conditionForNextStep,
        column.isFirstStep -> entity.isFirstStep,
        column.isLastStep -> entity.isLastStep,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: WorkflowEngine)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(WorkflowEngine).where.eq(column.id, entity.id) }.update.apply()
  }

}
