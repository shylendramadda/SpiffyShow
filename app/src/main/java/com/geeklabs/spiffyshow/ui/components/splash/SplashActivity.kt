package com.geeklabs.spiffyshow.ui.components.splash

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.extensions.launchActivity
import com.geeklabs.spiffyshow.extensions.withDelay
import com.geeklabs.spiffyshow.ui.base.BaseActivity
import com.geeklabs.spiffyshow.ui.components.login.LoginActivity
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import com.geeklabs.spiffyshow.ui.components.onboard.OnbaordActivity
import com.geeklabs.spiffyshow.utils.Utils
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject


class SplashActivity : BaseActivity<SplashContract.View, SplashContract.Presenter>(),
    SplashContract.View {

    @Inject
    lateinit var splashPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
    }

    override fun initUI() {
    }

    override fun showNoInternetSnackBar() {
        Utils.showNoInternetWarning(rootView, this)
    }

    override fun navigateToLoginScreen() {
        launchActivity<LoginActivity> { }
    }

    override fun navigateToMainScreen() {
        launchActivity<MainActivity> { }
    }

    override fun navigateToWelcomeScreen() {
        launchActivity<OnbaordActivity> { }
    }

    override fun finishView() {
        finish()
    }

    override fun onResume() {
        withDelay(SPLASH_DELAY) {
            presenter?.onDelayFinished()
        }
        super.onResume()
    }

    override fun initPresenter() = splashPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.activity_splash

    companion object {
        private const val SPLASH_DELAY: Long = 2000 //1 second
    }

}