package com.geeklabs.spiffyshow.ui.components.onboard

import androidx.annotation.VisibleForTesting
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.models.WelcomeScreen
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.PrefManager
import javax.inject.Inject

class OnboardPresenter @Inject constructor(private val prefManager: PrefManager) :
    BasePresenter<OnboardContract.View>(), OnboardContract.Presenter {

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
            WelcomeScreen(
                1,
                "Trim Videos",
                "SpiffyShow a great platform",
                R.drawable.on_board_one
            ),
            WelcomeScreen(
                2,
                "Original Videos",
                "SpiffyShow a great platform",
                R.drawable.on_board_two
            ),
            WelcomeScreen(
                3,
                "Share Videos",
                "SpiffyShow a great platform",
                R.drawable.on_board_three
            )
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