package com.geeklabs.spiffyshow.ui.components.splash

import com.geeklabs.spiffyshow.ui.base.BaseContract

interface SplashContract {
    interface View : BaseContract.View {
        fun initUI()
        fun navigateToWelcomeScreen()
        fun navigateToMainScreen()
        fun navigateToLoginScreen()
        fun finishView()
        fun showNoInternetSnackBar()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onDelayFinished()
    }
}