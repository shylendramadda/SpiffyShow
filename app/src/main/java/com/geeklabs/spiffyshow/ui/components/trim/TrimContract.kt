package com.geeklabs.spiffyshow.ui.components.trim

import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface TrimContract {
    interface View : BaseContract.View {
        fun initUI()
        fun showToast(title: String)
        fun navigateToHome()
        fun showProgress()
        fun hideProgress()
        fun navigateToOriginals()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onCancelClick()
        fun setFileMetaData(fileMetaData: FileMetaData)
        fun onVideoTrimStarted()
        fun onVideoPrepared()
        fun onGetResult(uri: String?)
        fun onSaveClicked(
            title: String,
            description: String,
            category: String,
            isTrim: Boolean
        )
    }
}