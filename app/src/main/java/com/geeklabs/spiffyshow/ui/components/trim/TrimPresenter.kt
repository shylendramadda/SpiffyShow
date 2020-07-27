package com.geeklabs.spiffyshow.ui.components.trim

import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.domain.local.original.SaveUpdateOriginalsInLocalUseCase
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
    private val saveUpdateOriginalsInLocalUseCase: SaveUpdateOriginalsInLocalUseCase
) : BasePresenter<TrimContract.View>(),
    TrimContract.Presenter {

    private var processedUri: String? = null
    private var obj: Any? = null
    private var original: Original? = null
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
            val item = obj as Original
            this.original = item
            this.fileMetaData = item.fileMetaData
        }
    }

    override fun onGetResult(uri: String?) {
        try {
            if (uri?.isEmpty() == true) {
                getView()?.showToast("Unable to get the file path")
            } else {
                this.processedUri = uri
                getView()?.showToast("Processed and trimmed video successfully")
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
        originalUrl: String,
        isTrim: Boolean
    ) {
        when {
            fileMetaData == null && externalUri.isEmpty() -> getView()?.showToast("Please enter the URL")
            fileMetaData == null && externalUri.isNotEmpty() && !isValidYoutubeUrl(externalUri) -> getView()?.showToast(
                "Please enter a valid youtube URL"
            )
            title.isEmpty() -> getView()?.showToast("Please enter a title")
            description.isEmpty() -> getView()?.showToast("Please enter a description")
            category.isEmpty() -> getView()?.showToast("Please enter a category")
            originalUrl.isNotEmpty() && !isValidURL(originalUrl) -> getView()?.showToast("Please enter a valid original URL")
            else -> {
                if (trim != null || isTrim) { // save trimmed video
                    if (processedUri == null && isTrim) {
                        getView()?.showToast("Please trim the video and save before submit")
                        return
                    } else if (processedUri != null) {
                        fileMetaData?.path = processedUri ?: ""
                    }
                    val trim = Trim(
                        id = this.trim?.id ?: 0,
                        title = title,
                        description = description,
                        category = category,
                        originalUrl = originalUrl,
                        fileMetaData = fileMetaData!!,
                        time = Utils.getCurrentTime(),
                        userId = applicationState.user?.id ?: 0
                    )
                    Observable.fromCallable {
                        saveUpdateTrimInLocalUseCase.execute(mutableListOf(trim))
                    }.subscribeOn(Schedulers.newThread()).subscribe()
                    getView()?.navigateToHome()
                } else { // update original video
                    if (externalUri.isNotEmpty()) {
                        fileMetaData = FileMetaData(path = externalUri)
                    }
                    val item = Original(
                        id = this.original?.id ?: 0,
                        title = title,
                        description = description,
                        category = category,
                        originalUrl = originalUrl,
                        fileMetaData = fileMetaData,
                        time = Utils.getCurrentTime(),
                        userId = applicationState.user?.id ?: 0
                    )
                    Observable.fromCallable {
                        saveUpdateOriginalsInLocalUseCase.execute(mutableListOf(item))
                    }.subscribeOn(Schedulers.newThread()).subscribe()
                    getView()?.navigateToOriginals()
                }
                getView()?.showHideProgress(false)
            }
        }
    }
}