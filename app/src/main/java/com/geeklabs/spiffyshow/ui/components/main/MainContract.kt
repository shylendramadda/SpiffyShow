package com.geeklabs.spiffyshow.ui.components.main

import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface MainContract {

    interface View : BaseContract.View {
        fun initUI()
        fun startWorkManager()
        fun showDrawer()
        fun navigateToLogin()
        fun showToast(message: String)
        fun stopService()
        fun navigateToHome()
        fun startVideoIntent()
        fun navigateToTrim(fileMetaData: FileMetaData)
        fun askPermissions()
        fun navigateToOriginal(fileMetaData: FileMetaData)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onMenuButtonClicked()
        fun onAddButtonClicked(isPermissionEnable: Boolean)
        fun onSaveFilePath(fileUri: String?)
    }
}