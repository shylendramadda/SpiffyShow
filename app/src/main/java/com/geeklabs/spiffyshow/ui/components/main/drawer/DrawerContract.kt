package com.geeklabs.spiffyshow.ui.components.main.drawer

import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface DrawerContract {

    interface View : BaseContract.View {
        fun initUI()
        fun logoutFirebaseNavigateToLogin()
        fun stopService()
        fun showShareIntent()
        fun showToast(message: String)
        fun navigateToHome()
        fun navigateToAboutScreen()
        fun closeDrawer()
        fun navigateToFeedback()
        fun showUserDetails(user: User?)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onHomeClicked()
        fun onBackClicked()
        fun onFeedbackClicked()
        fun onAboutClicked()
        fun onLogoutClicked()
        fun onShareClicked()
    }
}