package com.geeklabs.spiffyshow.ui.components.main.drawer

import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.PrefManager
import javax.inject.Inject

class DrawerPresenter @Inject constructor(
    private val prefManager: PrefManager,
    private val applicationState: ApplicationState
) : BasePresenter<DrawerContract.View>(),
    DrawerContract.Presenter {

    private var user: User? = null

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
        loadUserFromLocal()
    }

    private fun loadUserFromLocal() {
        user = applicationState.user
        getView()?.showUserDetails(user)
    }

    override fun onHomeClicked() {
        getView()?.navigateToHome()
    }

    override fun onBackClicked() {
        getView()?.closeDrawer()
    }

    override fun onFeedbackClicked() {
        getView()?.navigateToFeedback()
    }

    override fun onAboutClicked() {
        getView()?.navigateToAboutScreen()
    }

    override fun onLogoutClicked() {
        applicationState.clear()
        prefManager.save(Constants.IS_LOGIN, false)
        getView()?.stopService()
        getView()?.logoutFirebaseNavigateToLogin()
    }

    override fun onShareClicked() {
        getView()?.showShareIntent()
    }
}