package com.geeklabs.spiffyshow.ui.components.main

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
        fun askPermissions()
        fun navigateToTrim(obj: Any, isTrim: Boolean)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onMenuButtonClicked()
        fun onAddButtonClicked(isPermissionEnable: Boolean)
        fun onSaveFilePath(fileUri: String?)
    }
}