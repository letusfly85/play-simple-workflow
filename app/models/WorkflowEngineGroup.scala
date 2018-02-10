package models

import scalikejdbc._
import org.joda.time.{DateTime}

case class WorkflowEngineGroup(
  id: Int,
  workflowGroupId: Int,
  workflowId: Int,
  beforeWorkflowId: Option[Int] = None,
  conditionToStart: Option[String] = None,
  createdAt: DateTime,
  updatedAt: DateTime) {

  def save()(implicit session: DBSession = WorkflowEngineGroup.autoSession): WorkflowEngineGroup = WorkflowEngineGroup.save(this)(session)

  def destroy()(implicit session: DBSession = WorkflowEngineGroup.autoSession): Int = WorkflowEngineGroup.destroy(this)(session)

}


object WorkflowEngineGroup extends SQLSyntaxSupport[WorkflowEngineGroup] {

  override val schemaName = Some("simple_workflow")

  override val tableName = "workflow_engine_group"

  override val columns = Seq("id", "workflow_group_id", "workflow_id", "before_workflow_id", "condition_to_start", "created_at", "updated_at")

  def apply(weg: SyntaxProvider[WorkflowEngineGroup])(rs: WrappedResultSet): WorkflowEngineGroup = apply(weg.resultName)(rs)
  def apply(weg: ResultName[WorkflowEngineGroup])(rs: WrappedResultSet): WorkflowEngineGroup = new WorkflowEngineGroup(
    id = rs.get(weg.id),
    workflowGroupId = rs.get(weg.workflowGroupId),
    workflowId = rs.get(weg.workflowId),
    beforeWorkflowId = rs.get(weg.beforeWorkflowId),
    conditionToStart = rs.get(weg.conditionToStart),
    createdAt = rs.get(weg.createdAt),
    updatedAt = rs.get(weg.updatedAt)
  )

  val weg = WorkflowEngineGroup.syntax("weg")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[WorkflowEngineGroup] = {
    withSQL {
      select.from(WorkflowEngineGroup as weg).where.eq(weg.id, id)
    }.map(WorkflowEngineGroup(weg.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[WorkflowEngineGroup] = {
    withSQL(select.from(WorkflowEngineGroup as weg)).map(WorkflowEngineGroup(weg.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(WorkflowEngineGroup as weg)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[WorkflowEngineGroup] = {
    withSQL {
      select.from(WorkflowEngineGroup as weg).where.append(where)
    }.map(WorkflowEngineGroup(weg.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[WorkflowEngineGroup] = {
    withSQL {
      select.from(WorkflowEngineGroup as weg).where.append(where)
    }.map(WorkflowEngineGroup(weg.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(WorkflowEngineGroup as weg).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    workflowGroupId: Int,
    workflowId: Int,
    beforeWorkflowId: Option[Int] = None,
    conditionToStart: Option[String] = None,
    createdAt: DateTime,
    updatedAt: DateTime)(implicit session: DBSession = autoSession): WorkflowEngineGroup = {
    val generatedKey = withSQL {
      insert.into(WorkflowEngineGroup).namedValues(
        column.workflowGroupId -> workflowGroupId,
        column.workflowId -> workflowId,
        column.beforeWorkflowId -> beforeWorkflowId,
        column.conditionToStart -> conditionToStart,
        column.createdAt -> createdAt,
        column.updatedAt -> updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    WorkflowEngineGroup(
      id = generatedKey.toInt,
      workflowGroupId = workflowGroupId,
      workflowId = workflowId,
      beforeWorkflowId = beforeWorkflowId,
      conditionToStart = conditionToStart,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: Seq[WorkflowEngineGroup])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'workflowGroupId -> entity.workflowGroupId,
        'workflowId -> entity.workflowId,
        'beforeWorkflowId -> entity.beforeWorkflowId,
        'conditionToStart -> entity.conditionToStart,
        'createdAt -> entity.createdAt,
        'updatedAt -> entity.updatedAt))
    SQL("""insert into workflow_engine_group(
      workflow_group_id,
      workflow_id,
      before_workflow_id,
      condition_to_start,
      created_at,
      updated_at
    ) values (
      {workflowGroupId},
      {workflowId},
      {beforeWorkflowId},
      {conditionToStart},
      {createdAt},
      {updatedAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: WorkflowEngineGroup)(implicit session: DBSession = autoSession): WorkflowEngineGroup = {
    withSQL {
      update(WorkflowEngineGroup).set(
        column.id -> entity.id,
        column.workflowGroupId -> entity.workflowGroupId,
        column.workflowId -> entity.workflowId,
        column.beforeWorkflowId -> entity.beforeWorkflowId,
        column.conditionToStart -> entity.conditionToStart,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: WorkflowEngineGroup)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(WorkflowEngineGroup).where.eq(column.id, entity.id) }.update.apply()
  }

}
