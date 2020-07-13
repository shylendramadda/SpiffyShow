package com.geeklabs.spiffyshow.ui.components.trim

import com.geeklabs.spiffyshow.data.local.models.item.Trim
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
    private val saveUpdateTrimInLocalUseCase: SaveUpdateTrimInLocalUseCase
) : BasePresenter<TrimContract.View>(),
    TrimContract.Presenter {

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
        }
    }

    override fun onSaveClicked(
        uri: String?,
        title: String,
        description: String,
        category: String
    ) {
        if (uri?.isEmpty() == true) {
            getView()?.showToast("Unable to get the file path")
            return
        }
        fileMetaData.uri = uri ?: ""
        when {
            title.isEmpty() -> getView()?.showToast("Please enter title")
            description.isEmpty() -> getView()?.showToast("Please enter description")
            category.isEmpty() -> getView()?.showToast("Please enter category")
            else -> {
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
                getView()?.hideProgress()
            }
        }
    }

    override fun onVideoPrepared() {
        getView()?.hideProgress()
        getView()?.showToast("Processed video successfully")
    }

    override fun onCancelClick() {
        getView()?.navigateToHome()
    }
}