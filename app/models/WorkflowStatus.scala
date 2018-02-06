package models

import scalikejdbc._
import org.joda.time.{DateTime}

case class WorkflowStatus(
  id: Int,
  workflowId: Option[Int] = None,
  workflowStepId: Option[Int] = None,
  userId: Int,
  isExecuted: Boolean,
  createdAt: DateTime,
  updatedAt: DateTime) {

  def save()(implicit session: DBSession = WorkflowStatus.autoSession): WorkflowStatus = WorkflowStatus.save(this)(session)

  def destroy()(implicit session: DBSession = WorkflowStatus.autoSession): Int = WorkflowStatus.destroy(this)(session)

}


object WorkflowStatus extends SQLSyntaxSupport[WorkflowStatus] {

  override val schemaName = Some("simple_workflow")

  override val tableName = "workflow_status"

  override val columns = Seq("id", "workflow_id", "workflow_step_id", "user_id", "is_executed", "created_at", "updated_at")

  def apply(ws: SyntaxProvider[WorkflowStatus])(rs: WrappedResultSet): WorkflowStatus = apply(ws.resultName)(rs)
  def apply(ws: ResultName[WorkflowStatus])(rs: WrappedResultSet): WorkflowStatus = new WorkflowStatus(
    id = rs.get(ws.id),
    workflowId = rs.get(ws.workflowId),
    workflowStepId = rs.get(ws.workflowStepId),
    userId = rs.get(ws.userId),
    isExecuted = rs.get(ws.isExecuted),
    createdAt = rs.get(ws.createdAt),
    updatedAt = rs.get(ws.updatedAt)
  )

  val ws = WorkflowStatus.syntax("ws")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[WorkflowStatus] = {
    withSQL {
      select.from(WorkflowStatus as ws).where.eq(ws.id, id)
    }.map(WorkflowStatus(ws.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[WorkflowStatus] = {
    withSQL(select.from(WorkflowStatus as ws)).map(WorkflowStatus(ws.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(WorkflowStatus as ws)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[WorkflowStatus] = {
    withSQL {
      select.from(WorkflowStatus as ws).where.append(where)
    }.map(WorkflowStatus(ws.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[WorkflowStatus] = {
    withSQL {
      select.from(WorkflowStatus as ws).where.append(where)
    }.map(WorkflowStatus(ws.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(WorkflowStatus as ws).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    workflowId: Option[Int] = None,
    workflowStepId: Option[Int] = None,
    userId: Int,
    isExecuted: Boolean,
    createdAt: DateTime,
    updatedAt: DateTime)(implicit session: DBSession = autoSession): WorkflowStatus = {
    val generatedKey = withSQL {
      insert.into(WorkflowStatus).namedValues(
        column.workflowId -> workflowId,
        column.workflowStepId -> workflowStepId,
        column.userId -> userId,
        column.isExecuted -> isExecuted,
        column.createdAt -> createdAt,
        column.updatedAt -> updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    WorkflowStatus(
      id = generatedKey.toInt,
      workflowId = workflowId,
      workflowStepId = workflowStepId,
      userId = userId,
      isExecuted = isExecuted,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: Seq[WorkflowStatus])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'workflowId -> entity.workflowId,
        'workflowStepId -> entity.workflowStepId,
        'userId -> entity.userId,
        'isExecuted -> entity.isExecuted,
        'createdAt -> entity.createdAt,
        'updatedAt -> entity.updatedAt))
    SQL("""insert into workflow_status(
      workflow_id,
      workflow_step_id,
      user_id,
      is_executed,
      created_at,
      updated_at
    ) values (
      {workflowId},
      {workflowStepId},
      {userId},
      {isExecuted},
      {createdAt},
      {updatedAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: WorkflowStatus)(implicit session: DBSession = autoSession): WorkflowStatus = {
    withSQL {
      update(WorkflowStatus).set(
        column.id -> entity.id,
        column.workflowId -> entity.workflowId,
        column.workflowStepId -> entity.workflowStepId,
        column.userId -> entity.userId,
        column.isExecuted -> entity.isExecuted,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: WorkflowStatus)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(WorkflowStatus).where.eq(column.id, entity.id) }.update.apply()
  }

}
