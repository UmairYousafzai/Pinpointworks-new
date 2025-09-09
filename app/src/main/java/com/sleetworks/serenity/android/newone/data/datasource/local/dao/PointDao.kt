package com.sleetworks.serenity.android.newone.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.sleetworks.serenity.android.newone.data.models.local.entities.PointEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.ShareEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PointDao {

    @Upsert
    suspend fun insertPoint(share: PointEntity)

    @Upsert
    suspend fun insertPoints(points: List<PointEntity>)

    @Query("SELECT * FROM point WHERE id = :pointID")
    suspend fun getPointById(pointID: String): PointEntity?

    @Query("SELECT * FROM point WHERE local_id = :localID")
    suspend fun getPointByLocalId(localID: String): PointEntity?

    @Query("SELECT * FROM point WHERE workspace_id = :workspaceID")
     fun getPointByWorkspaceId(workspaceID: String): Flow<List<PointEntity>>

    @Query("SELECT * FROM point")
    suspend fun getAllPoints(): List<PointEntity?>

    @Query("DELETE FROM point WHERE id IN (:pointIds)")
    suspend fun deletePointsByIds(pointIds: List<String>):Int

    @Query("DELETE FROM point WHERE workspace_id IN (:workspaceID)")
    suspend fun deletePointsByWorkspaceId(workspaceID: String):Int
}