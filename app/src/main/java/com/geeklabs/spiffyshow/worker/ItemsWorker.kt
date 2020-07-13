package com.geeklabs.spiffyshow.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.geeklabs.spiffyshow.App
import com.geeklabs.spiffyshow.domain.local.item.SaveUpdateItemsInLocalUseCase
import com.geeklabs.spiffyshow.domain.remote.item.FetchItemsRemoteUseCase
import com.log4k.e
import javax.inject.Inject

class ItemsWorker(appContext: Context, workerParams: WorkerParameters) :
    BaseWorker(appContext, workerParams) {

    @Inject
    lateinit var fetchItemRemoteUseCase: FetchItemsRemoteUseCase

    @Inject
    lateinit var saveUpdateItemsLocalUseCase: SaveUpdateItemsInLocalUseCase

    init {
        (applicationContext as App).applicationComponent.inject(this)
    }

    override fun runWorker() {
        try {
            // TODO
            /*val itemList = fetchItemRemoteUseCase.execute(Unit).blockingGet()
            if (itemList.isNotEmpty()) {
                saveUpdateItemsLocalUseCase.execute(itemList)
            }*/
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onWorkerException(e: Exception) {
        e("CategoryWorker Exception Message-> ${e.message}")
    }
}