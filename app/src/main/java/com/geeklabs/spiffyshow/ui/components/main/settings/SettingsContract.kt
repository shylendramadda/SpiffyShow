package com.geeklabs.spiffyshow.ui.components.main.settings

import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface SettingsContract {
    interface View : BaseContract.View {
        fun initUI()
        fun askPermissions()
        fun showUploadImageDialog()
        fun showToast(message: String)
        fun showUserDetails(user: User)
        fun navigateToHome()
        fun updateProfileImage(filePath: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onEditImageClicked(permissionEnable: Boolean)
        fun onSaveFilePath(imagePath: String)
        fun saveUpdateUser(user: User)
    }
}