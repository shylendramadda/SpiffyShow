package com.geeklabs.spiffyshow.worker

import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.Worker

abstract class BaseWorker constructor(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    abstract fun runWorker()
    abstract fun onWorkerException(e: Exception)

    override fun doWork(): Result {
        try {
            runWorker()
        } catch (e: Exception) {
            onWorkerException(e)
            if (runAttemptCount < MAX_RETRY_COUNT) {
                return Result.retry()
            }
        }
        return Result.success()
    }

    companion object {
        const val MAX_RETRY_COUNT = 3
    }
}