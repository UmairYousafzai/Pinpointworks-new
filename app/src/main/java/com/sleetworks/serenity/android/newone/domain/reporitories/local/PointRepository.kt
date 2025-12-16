package com.sleetworks.serenity.android.newone.domain.reporitories.local

import com.sleetworks.serenity.android.newone.data.models.local.PointWithRelations
import com.sleetworks.serenity.android.newone.data.models.local.entities.OfflineModifiedPointFields
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Point
import com.sleetworks.serenity.android.newone.domain.models.point.PointDomain
import kotlinx.coroutines.flow.Flow

interface PointRepository {

    suspend fun insertPoint(point: Point)
    suspend fun insertPoint(point: PointDomain)
    suspend fun insertPoints(points: List<Point>)
    suspend fun insertModifiedField(field: OfflineModifiedPointFields)
    suspend fun getPointByID(pointID: String): PointEntity?
    suspend fun getPointByIDFlow(pointID: String): Flow<PointWithRelations>
    suspend fun getPointByLocalID(localID: String): PointEntity?
    suspend fun getPointByWorkspaceIDFlow(workspaceID: String): Flow<List<PointWithRelations>>
    suspend fun getPointByWorkspaceID(workspaceID: String): List<PointWithRelations>
    suspend fun getAllPoints(): List<PointEntity?>
    suspend fun getAllModifiedFields(pointID: String): List<OfflineModifiedPointFields?>
    suspend fun getAllModifiedFieldsFlow(pointID: String): Flow<List<OfflineModifiedPointFields>>
    suspend fun deletePointByID(pointIDs: List<String>): Int
    suspend fun deletePointByWorkspaceID(workspaceID: String): Int
    suspend fun deleteModifiedFieldByPointId(pointId: String): Int
}