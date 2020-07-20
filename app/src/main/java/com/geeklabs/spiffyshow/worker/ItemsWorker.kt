package com.geeklabs.spiffyshow.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.geeklabs.spiffyshow.App
import com.geeklabs.spiffyshow.domain.local.original.SaveUpdateOriginalsInLocalUseCase
import com.geeklabs.spiffyshow.domain.remote.original.FetchOriginalsRemoteUseCase
import com.log4k.e
import javax.inject.Inject

class ItemsWorker(appContext: Context, workerParams: WorkerParameters) :
    BaseWorker(appContext, workerParams) {

    @Inject
    lateinit var fetchItemRemoteUseCase: FetchOriginalsRemoteUseCase

    @Inject
    lateinit var saveUpdateOriginalsLocalUseCase: SaveUpdateOriginalsInLocalUseCase

    init {
        (applicationContext as App).applicationComponent.inject(this)
    }

    override fun runWorker() {
        try {
            // TODO
            /*val itemList = fetchItemRemoteUseCase.execute(Unit).blockingGet()
            if (itemList.isNotEmpty()) {
                saveUpdateOriginalsLocalUseCase.execute(itemList)
            }*/
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onWorkerException(e: Exception) {
        e("CategoryWorker Exception Message-> ${e.message}")
    }
}