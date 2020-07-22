package com.geeklabs.spiffyshow.ui.components.splash

import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.PrefManager
import javax.inject.Inject

class SplashPresenter @Inject constructor(
    private val prefManager: PrefManager
) : BasePresenter<SplashContract.View>(),
    SplashContract.Presenter {

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
    }

    override fun onDelayFinished() {
        val isWelcomeLoaded = prefManager.getBoolean(Constants.IS_WELCOME_SEEN, false)
        val isLogin = prefManager.getBoolean(Constants.IS_LOGIN, false)
        when {
            isLogin -> getView()?.navigateToMainScreen()
            !isNetworkConnected() -> getView()?.showNoInternetSnackBar()
            !isWelcomeLoaded -> getView()?.navigateToWelcomeScreen()
            else -> getView()?.navigateToLoginScreen()
        }
        if (isNetworkConnected() || isLogin) {
            getView()?.finishView()
        }
    }
}