package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sleetworks.serenity.android.newone.data.models.local.InsertPoint
import com.sleetworks.serenity.android.newone.data.models.local.PointWithRelations
import com.sleetworks.serenity.android.newone.data.models.local.entities.OfflineModifiedPointFields
import com.sleetworks.serenity.android.newone.data.models.local.entities.customField.PointCustomFieldEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointAssigneeEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoint(point: PointEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoints(points: List<PointEntity>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModifiedField(field: OfflineModifiedPointFields)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPointTags(sites: List<PointTagEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPointAssignees(sites: List<PointAssigneeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPointCustomFields(customFields: List<PointCustomFieldEntity>)

    @Transaction
    suspend fun upsertPointWithChildren(
        insertPoint: InsertPoint
    ) {
        insertPoint(insertPoint.point)                 // parent first
        if (insertPoint.assignees.isNotEmpty()) insertPointAssignees(insertPoint.assignees)
        if (insertPoint.tags.isNotEmpty()) insertPointTags(insertPoint.tags)
        if (insertPoint.customFields.isNotEmpty()) insertPointCustomFields(insertPoint.customFields)
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

    @Transaction
    @Query("SELECT * FROM point WHERE id = :pointID")
    fun getPointByIdFlow(pointID: String): Flow<PointWithRelations>

    @Query("SELECT * FROM point WHERE local_id = :localID")
    suspend fun getPointByLocalId(localID: String): PointEntity?

    @Transaction
    @Query("SELECT * FROM point WHERE workspace_id = :workspaceID ORDER BY updatedAt DESC")
    fun getPointByWorkspaceIdFlow(workspaceID: String): Flow<List<PointWithRelations>>

    @Transaction
    @Query("SELECT * FROM point WHERE workspace_id = :workspaceID ORDER BY updatedAt DESC")
    fun getPointByWorkspaceId(workspaceID: String): List<PointWithRelations>

    @Query("SELECT * FROM point")
    suspend fun getAllPoints(): List<PointEntity?>

    @Query("SELECT * FROM offline_modified_point_fields WHERE pointId = :pointID")
    suspend fun getAllOfflineModifiedFields(pointID: String): List<OfflineModifiedPointFields?>

    @Query("SELECT * FROM offline_modified_point_fields WHERE pointId = :pointID")
    fun getAllOfflineModifiedFieldsFlow(pointID: String): Flow<List<OfflineModifiedPointFields>>

    @Query("DELETE FROM point WHERE id IN (:pointIds)")
    suspend fun deletePointsByIds(pointIds: List<String>): Int

    @Query("DELETE FROM point WHERE workspace_id IN (:workspaceID)")
    suspend fun deletePointsByWorkspaceId(workspaceID: String): Int

    @Query("DELETE FROM offline_modified_point_fields WHERE pointId IN (:pointId)")
    suspend fun deleteModifiedFieldByPointId(pointId: String): Int

    @Query("DELETE FROM offline_modified_point_fields WHERE id IN (:id)")
    suspend fun deleteModifiedFieldById(id: Int): Int

    @Query("DELETE FROM offline_modified_point_fields WHERE field IN (:value)")
    suspend fun deleteModifiedFieldByFieldValue(value: String): Int
}