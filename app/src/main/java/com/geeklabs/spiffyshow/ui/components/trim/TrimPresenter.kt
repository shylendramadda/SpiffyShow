package com.geeklabs.spiffyshow.ui.components.trim

import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.domain.local.item.SaveUpdateItemsInLocalUseCase
import com.geeklabs.spiffyshow.domain.local.trim.SaveUpdateTrimInLocalUseCase
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Utils
import com.geeklabs.spiffyshow.utils.Utils.isValidURL
import com.geeklabs.spiffyshow.utils.Utils.isValidYoutubeUrl
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
    private var obj: Any? = null
    private var item: Item? = null
    private var trim: Trim? = null
    private var fileMetaData: FileMetaData? = null

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
    }

    override fun onVideoTrimStarted() {
        getView()?.showHideProgress(true)
    }

    override fun setItem(obj: Any) {
        this.obj = obj
        if (obj is Trim) {
            this.trim = obj
            this.fileMetaData = obj.fileMetaData
        } else {
            val item = obj as Item
            this.item = item
            this.fileMetaData = item.fileMetaData
        }
    }

    override fun onGetResult(uri: String?) {
        try {
            if (uri?.isEmpty() == true) {
                getView()?.showToast("Unable to get the file path")
            } else {
                this.processedUri = uri
                getView()?.showToast("Processed video successfully")
            }
        } catch (e: Exception) {
            getView()?.showToast("Problem occurred while processing the video")
            e.printStackTrace()
        }
        getView()?.showHideProgress(false)
    }

    override fun onVideoPrepared() {
        getView()?.showHideProgress(false)
        getView()?.showToast("Video prepared")
    }

    override fun onCancelClick() {
        getView()?.navigateToHome()
    }

    override fun onSaveClicked(
        externalUri: String,
        title: String,
        description: String,
        category: String,
        isTrim: Boolean
    ) {
        when {
            fileMetaData == null && externalUri.isEmpty() -> getView()?.showToast("Please enter URL")
            fileMetaData == null && externalUri.isNotEmpty() && !isValidYoutubeUrl(externalUri) -> getView()?.showToast(
                "Please enter valid youtube URL"
            )
            title.isEmpty() -> getView()?.showToast("Please enter title")
            description.isEmpty() -> getView()?.showToast("Please enter description")
            category.isEmpty() -> getView()?.showToast("Please enter category")
            else -> {
                if (trim != null || isTrim) {
                    if (processedUri?.isEmpty() == false) {
                        fileMetaData?.path = processedUri ?: ""
                    }
                    val trim = Trim(
                        id = this.trim?.id ?: 0,
                        title = title,
                        description = description,
                        category = category,
                        fileMetaData = fileMetaData!!,
                        time = Utils.getCurrentTime(),
                        userId = applicationState.user?.id ?: 0
                    )
                    Observable.fromCallable {
                        saveUpdateTrimInLocalUseCase.execute(mutableListOf(trim))
                    }.subscribeOn(Schedulers.newThread()).subscribe()
                    getView()?.navigateToHome()
                } else {
                    if (externalUri.isNotEmpty()) {
                        fileMetaData = FileMetaData(path = externalUri)
                    }
                    val item = Item(
                        id = this.item?.id ?: 0,
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
                getView()?.showHideProgress(false)
            }
        }
    }
}