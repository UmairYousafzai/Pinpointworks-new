package com.sleetworks.serenity.android.newone.data.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import javax.inject.Inject

class DefectSyncWorker @Inject constructor(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {

        return try {
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

}