package models

import scalikejdbc._
import org.joda.time.{DateTime}

case class WorkflowStatusGroup(
  id: Int,
  userId: Int,
  workflowGroupId: Int,
  workflowId: Int,
  runningStatus: Int,
  createdAt: DateTime,
  updatedAt: DateTime) {

  def save()(implicit session: DBSession = WorkflowStatusGroup.autoSession): WorkflowStatusGroup = WorkflowStatusGroup.save(this)(session)

  def destroy()(implicit session: DBSession = WorkflowStatusGroup.autoSession): Int = WorkflowStatusGroup.destroy(this)(session)

}


object WorkflowStatusGroup extends SQLSyntaxSupport[WorkflowStatusGroup] {

  override val schemaName = Some("simple_workflow")

  override val tableName = "workflow_status_group"

  override val columns = Seq("id", "user_id", "workflow_group_id", "workflow_id", "running_status", "created_at", "updated_at")

  def apply(wsg: SyntaxProvider[WorkflowStatusGroup])(rs: WrappedResultSet): WorkflowStatusGroup = apply(wsg.resultName)(rs)
  def apply(wsg: ResultName[WorkflowStatusGroup])(rs: WrappedResultSet): WorkflowStatusGroup = new WorkflowStatusGroup(
    id = rs.get(wsg.id),
    userId = rs.get(wsg.userId),
    workflowGroupId = rs.get(wsg.workflowGroupId),
    workflowId = rs.get(wsg.workflowId),
    runningStatus = rs.get(wsg.runningStatus),
    createdAt = rs.get(wsg.createdAt),
    updatedAt = rs.get(wsg.updatedAt)
  )

  val wsg = WorkflowStatusGroup.syntax("wsg")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[WorkflowStatusGroup] = {
    withSQL {
      select.from(WorkflowStatusGroup as wsg).where.eq(wsg.id, id)
    }.map(WorkflowStatusGroup(wsg.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[WorkflowStatusGroup] = {
    withSQL(select.from(WorkflowStatusGroup as wsg)).map(WorkflowStatusGroup(wsg.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(WorkflowStatusGroup as wsg)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[WorkflowStatusGroup] = {
    withSQL {
      select.from(WorkflowStatusGroup as wsg).where.append(where)
    }.map(WorkflowStatusGroup(wsg.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[WorkflowStatusGroup] = {
    withSQL {
      select.from(WorkflowStatusGroup as wsg).where.append(where)
    }.map(WorkflowStatusGroup(wsg.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(WorkflowStatusGroup as wsg).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userId: Int,
    workflowGroupId: Int,
    workflowId: Int,
    runningStatus: Int,
    createdAt: DateTime,
    updatedAt: DateTime)(implicit session: DBSession = autoSession): WorkflowStatusGroup = {
    val generatedKey = withSQL {
      insert.into(WorkflowStatusGroup).namedValues(
        column.userId -> userId,
        column.workflowGroupId -> workflowGroupId,
        column.workflowId -> workflowId,
        column.runningStatus -> runningStatus,
        column.createdAt -> createdAt,
        column.updatedAt -> updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    WorkflowStatusGroup(
      id = generatedKey.toInt,
      userId = userId,
      workflowGroupId = workflowGroupId,
      workflowId = workflowId,
      runningStatus = runningStatus,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: Seq[WorkflowStatusGroup])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'userId -> entity.userId,
        'workflowGroupId -> entity.workflowGroupId,
        'workflowId -> entity.workflowId,
        'runningStatus -> entity.runningStatus,
        'createdAt -> entity.createdAt,
        'updatedAt -> entity.updatedAt))
    SQL("""insert into workflow_status_group(
      user_id,
      workflow_group_id,
      workflow_id,
      running_status,
      created_at,
      updated_at
    ) values (
      {userId},
      {workflowGroupId},
      {workflowId},
      {runningStatus},
      {createdAt},
      {updatedAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: WorkflowStatusGroup)(implicit session: DBSession = autoSession): WorkflowStatusGroup = {
    withSQL {
      update(WorkflowStatusGroup).set(
        column.id -> entity.id,
        column.userId -> entity.userId,
        column.workflowGroupId -> entity.workflowGroupId,
        column.workflowId -> entity.workflowId,
        column.runningStatus -> entity.runningStatus,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: WorkflowStatusGroup)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(WorkflowStatusGroup).where.eq(column.id, entity.id) }.update.apply()
  }

}
