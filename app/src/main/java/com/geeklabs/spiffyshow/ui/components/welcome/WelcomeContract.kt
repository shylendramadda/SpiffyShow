package com.geeklabs.spiffyshow.ui.components.welcome

import com.geeklabs.spiffyshow.models.WelcomeScreen
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface WelcomeContract {

    interface View : BaseContract.View {

        fun initUI()

        fun showWelcomePages(welcomeScreens: MutableList<WelcomeScreen>)

        fun navigateToLoginScreen()

        fun addDot(index: Int, currentPosition: Int)

        fun removeAllViews()

        fun showHideGotItButton(isShow: Boolean)
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun onSkipOrGotItButtonClicked()

        fun onPageSelected(currentPosition: Int)
    }
}