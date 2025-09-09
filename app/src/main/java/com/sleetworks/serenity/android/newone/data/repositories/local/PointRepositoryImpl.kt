package com.sleetworks.serenity.android.newone.data.repositories.local

import android.util.Log
import com.google.gson.Gson
import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointDao
import com.sleetworks.serenity.android.newone.data.mappers.toEntity
import com.sleetworks.serenity.android.newone.data.models.local.entities.PointEntity
import com.sleetworks.serenity.android.newone.data.models.remote.response.point.Point
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PointRepositoryImpl @Inject constructor(val pointDao: PointDao) :
    PointRepository {

    override suspend fun insertPoint(point: Point) {
        pointDao.insertPoint(point.toEntity())
    }

    override suspend fun insertPoints(points: List<Point>) {
        val gson = Gson()
        val pointEntitys = points.map {
            it.toEntity()
        }
        Log.e("PointRepositoryImpl", "insertPoints: ${gson.toJson(pointEntitys)}")

        pointDao.insertPoints(pointEntitys);

    }

    override suspend fun getPointByID(pointID: String): PointEntity? {
        return pointDao.getPointById(pointID)
    }

    override suspend fun getPointByLocalID(localID: String): PointEntity? {
        return pointDao.getPointByLocalId(localID)
    }

    override suspend fun getPointByWorkspaceID(workspaceID: String): Flow<List<PointEntity?>> {
        return pointDao.getPointByWorkspaceId(workspaceID)
    }

    override suspend fun getAllPoints(): List<PointEntity?> {
        return pointDao.getAllPoints()
    }

    override suspend fun deletePointByID(pointIDs: List<String>): Int {
        return pointDao.deletePointsByIds(pointIDs)
    }

    override suspend fun deletePointByWorkspaceID(workspaceID: String): Int {
        return pointDao.deletePointsByWorkspaceId(workspaceID)
    }

}