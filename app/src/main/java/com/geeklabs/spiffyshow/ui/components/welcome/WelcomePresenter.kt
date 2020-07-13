package com.geeklabs.spiffyshow.ui.components.welcome

import androidx.annotation.VisibleForTesting
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.models.WelcomeScreen
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.PrefManager
import javax.inject.Inject

class WelcomePresenter @Inject constructor(private val prefManager: PrefManager) :
    BasePresenter<WelcomeContract.View>(), WelcomeContract.Presenter {

    @VisibleForTesting
    var list = mutableListOf<WelcomeScreen>()

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
        loadWelcomePages()
    }

    @VisibleForTesting
    fun loadWelcomePages() {
        list = mutableListOf(
            WelcomeScreen(1, "Trim Videos", "SpiffyShow a great platform1", R.drawable.app_logo),
            WelcomeScreen(2, "Original Videos", "SpiffyShow a great platform2", R.drawable.app_logo),
            WelcomeScreen(3, "Follow and get notify", "SpiffyShow a great platform3", R.drawable.app_logo)
        )
        getView()?.showWelcomePages(list)
        onPageSelected(0)
    }

    override fun onPageSelected(currentPosition: Int) {
        addDots(currentPosition)
    }

    @VisibleForTesting
    fun addDots(currentPosition: Int) {
        getView()?.removeAllViews()
        for (index in 0 until list.size) {
            getView()?.addDot(index, currentPosition)
            getView()?.showHideGotItButton(currentPosition == list.size - 1)
        }
    }

    override fun onSkipOrGotItButtonClicked() {
        prefManager.save(Constants.IS_WELCOME_SEEN, true)
        getView()?.navigateToLoginScreen()
    }

}