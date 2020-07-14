package com.geeklabs.spiffyshow.ui.components.trim

import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.domain.local.item.SaveUpdateItemsInLocalUseCase
import com.geeklabs.spiffyshow.domain.local.trim.SaveUpdateTrimInLocalUseCase
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Utils
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TrimPresenter @Inject constructor(
    private val applicationState: ApplicationState,
    private val saveUpdateTrimInLocalUseCase: SaveUpdateTrimInLocalUseCase,
    private val saveUpdateItemsInLocalUseCase: SaveUpdateItemsInLocalUseCase
) : BasePresenter<TrimContract.View>(),
    TrimContract.Presenter {

    private var processedUri: String? = null
    private lateinit var fileMetaData: FileMetaData

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
    }

    override fun onVideoTrimStarted() {
        getView()?.showProgress()
    }

    override fun setFileMetaData(fileMetaData: FileMetaData) {
        this.fileMetaData = fileMetaData
    }

    override fun onGetResult(uri: String?) {
        if (uri?.isEmpty() == true) {
            getView()?.showToast("Unable to get the file path")
        } else {
            this.processedUri = uri
            getView()?.showToast("Processed video successfully")
        }
        getView()?.hideProgress()
    }

    override fun onVideoPrepared() {
//        getView()?.showToast("Video prepared")
    }

    override fun onCancelClick() {
        getView()?.navigateToHome()
    }

    override fun onSaveClicked(
        title: String,
        description: String,
        category: String,
        isTrim: Boolean
    ) {
        when {
            title.isEmpty() -> getView()?.showToast("Please enter title")
            description.isEmpty() -> getView()?.showToast("Please enter description")
            category.isEmpty() -> getView()?.showToast("Please enter category")
            else -> {
                if (isTrim) {
                    if (processedUri?.isEmpty() == true) {
                        getView()?.showToast("Unable to get the file path")
                        return
                    }
                    fileMetaData.path = processedUri ?: ""
                    val trim = Trim(
                        title = title,
                        description = description,
                        category = category,
                        fileMetaData = fileMetaData,
                        time = Utils.getCurrentTime(),
                        userId = applicationState.user?.id ?: 0
                    )
                    Observable.fromCallable {
                        saveUpdateTrimInLocalUseCase.execute(mutableListOf(trim))
                    }.subscribeOn(Schedulers.newThread()).subscribe()
                    getView()?.navigateToHome()
                } else {
                    val item = Item(
                        title = title,
                        description = description,
                        category = category,
                        fileMetaData = fileMetaData,
                        time = Utils.getCurrentTime(),
                        userId = applicationState.user?.id ?: 0
                    )
                    Observable.fromCallable {
                        saveUpdateItemsInLocalUseCase.execute(mutableListOf(item))
                    }.subscribeOn(Schedulers.newThread()).subscribe()
                    getView()?.navigateToOriginals()
                }
                getView()?.hideProgress()
            }
        }
    }
}