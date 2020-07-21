package com.geeklabs.spiffyshow.ui.components.onboard

import android.content.res.ColorStateList
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.extensions.launchActivity
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.models.WelcomeScreen
import com.geeklabs.spiffyshow.ui.base.BaseActivity
import com.geeklabs.spiffyshow.ui.components.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_welcome.*
import javax.inject.Inject

class OnboardActivity : BaseActivity<OnboardContract.View, OnboardContract.Presenter>(),
    OnboardContract.View, ViewPager.OnPageChangeListener {

    @Inject
    lateinit var onboardPresenter: OnboardPresenter
    private lateinit var adapter: OnboardAdapter

    override fun initUI() {
        adapter = OnboardAdapter()
        welcomeViewPager.adapter = adapter
        welcomeViewPager.addOnPageChangeListener(this)
        skip.setOnClickListener {
            presenter?.onSkipOrGotItButtonClicked()
        }
        gotItButton.setOnClickListener {
            presenter?.onSkipOrGotItButtonClicked()
        }
    }

    override fun showWelcomePages(welcomeScreens: MutableList<WelcomeScreen>) {
        adapter.list = welcomeScreens
        adapter.notifyDataSetChanged()
    }

    override fun navigateToLoginScreen() {
        launchActivity<LoginActivity> { }
        finish()
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        presenter?.onPageSelected(position)
    }

    override fun addDot(index: Int, currentPosition: Int) {
        val radioButton = RadioButton(this)
        radioButton.id = index
        radioButton.scaleX = 0.3f
        radioButton.scaleY = 0.3f
        radioButton.isClickable = false
        radioButton.background = null
        radioButton.buttonDrawable =
            ContextCompat.getDrawable(this, R.drawable.circle)?.mutate()
        val bgColor: Int = if (index == currentPosition) {
            radioButton.scaleX = 0.4f
            radioButton.scaleY = 0.4f
            R.color.colorDarkBlue
        } else {
            R.color.grayPrimary
        }
        radioButton.buttonTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, bgColor))
        dotIndicatorRadioGroup.addView(radioButton)
    }

    override fun showHideGotItButton(isShow: Boolean) {
        gotItButton.visible = isShow
        skip.visible = !isShow
    }

    override fun removeAllViews() {
        dotIndicatorRadioGroup.removeAllViews()
    }

    override fun initPresenter() = onboardPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_welcome
}