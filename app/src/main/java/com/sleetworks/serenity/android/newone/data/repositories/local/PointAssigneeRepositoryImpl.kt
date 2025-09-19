package com.sleetworks.serenity.android.newone.data.repositories.local

import com.sleetworks.serenity.android.newone.data.datasource.local.dao.PointAssigneeDao
import com.sleetworks.serenity.android.newone.data.models.local.entities.point.PointAssigneeEntity
import com.sleetworks.serenity.android.newone.domain.reporitories.local.PointAssigneeRepository
import javax.inject.Inject

class PointAssigneeRepositoryImpl @Inject constructor(
    val assigneeDao: PointAssigneeDao,
) :
    PointAssigneeRepository {


    override suspend fun insertAssignee(assignee: PointAssigneeEntity) {
        assigneeDao.insertPointAssignee(assignee);
    }

    override suspend fun insertAssignees(assignees: List<PointAssigneeEntity>) {
        assigneeDao.insertPointAssignees(assignees)
    }

    override suspend fun getAssigneesByPointID(pointID: String): List<PointAssigneeEntity>? {
        return assigneeDao.getAssigneeByPointId(pointID)
    }

    override suspend fun getAllAssignees(): List<PointAssigneeEntity?> {
        return assigneeDao.getAllPointAssignees()
    }
}