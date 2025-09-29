package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.InsertPoint
import com.sleetworks.serenity.android.newone.data.models.local.PointWithRelations
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointAssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PointDao {

    @Upsert
    suspend fun insertPoint(share: PointEntity)

    @Upsert
    suspend fun insertPoints(points: List<PointEntity>)

    @Upsert
    suspend fun insertPointTags(sites: List<PointTagEntity>)

    @Upsert
    suspend fun insertPointAssignees(sites: List<PointAssigneeEntity>)

    @Upsert
    suspend fun insertPointCustomFields(customFields: List<PointCustomFieldEntity>)

    @Transaction
    suspend fun upsertPointWithChildren(
        point: PointEntity,
        assignees: List<PointAssigneeEntity>,
        tags: List<PointTagEntity>,
        customFields: List<PointCustomFieldEntity>
    ) {
        insertPoint(point)                 // parent first
        if (assignees.isNotEmpty()) insertPointAssignees(assignees)
        if (tags.isNotEmpty()) insertPointTags(tags)
        if (customFields.isNotEmpty()) insertPointCustomFields(customFields)
    }

    @Transaction
    suspend fun upsertPointsWithChildren(
        batch: List<InsertPoint>,
    ) {
        if (batch.isEmpty()) return
        insertPoints(batch.map { it.point })
        val assignees = batch.flatMap { it.assignees }
        val tags = batch.flatMap { it.tags }
        val customFields = batch.flatMap { it.customFields }
        if (assignees.isNotEmpty()) insertPointAssignees(assignees)
        if (tags.isNotEmpty()) insertPointTags(tags)
        if (customFields.isNotEmpty()) insertPointCustomFields(customFields)
    }

    @Query("SELECT * FROM point WHERE id = :pointID")
    suspend fun getPointById(pointID: String): PointEntity?

    @Query("SELECT * FROM point WHERE id = :pointID")
    suspend fun getPointByIdFlow(pointID: String): Flow<PointWithRelations>

    @Query("SELECT * FROM point WHERE local_id = :localID")
    suspend fun getPointByLocalId(localID: String): PointEntity?

    @Transaction
    @Query("SELECT * FROM point WHERE workspace_id = :workspaceID")
    fun getPointByWorkspaceId(workspaceID: String): Flow<List<PointWithRelations>>

    @Query("SELECT * FROM point")
    suspend fun getAllPoints(): List<PointEntity?>

    @Query("DELETE FROM point WHERE id IN (:pointIds)")
    suspend fun deletePointsByIds(pointIds: List<String>): Int

    @Query("DELETE FROM point WHERE workspace_id IN (:workspaceID)")
    suspend fun deletePointsByWorkspaceId(workspaceID: String): Int
}