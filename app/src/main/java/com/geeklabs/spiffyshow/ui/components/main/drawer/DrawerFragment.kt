package com.geeklabs.spiffyshow.ui.components.main.drawer

import android.content.Intent
import com.bumptech.glide.Glide
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.enums.Navigation
import com.geeklabs.spiffyshow.extensions.shouldShow
import com.geeklabs.spiffyshow.extensions.toast
import com.geeklabs.spiffyshow.service.AppService
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_drawer.*
import javax.inject.Inject

class DrawerFragment : BaseFragment<DrawerContract.View, DrawerContract.Presenter>(),
    DrawerContract.View {

    @Inject
    lateinit var drawerPresenter: DrawerPresenter

    override fun initUI() {
        home_nav.setOnClickListener { presenter?.onHomeClicked() }
        backButton.setOnClickListener { presenter?.onBackClicked() }
        feedback.setOnClickListener { presenter?.onFeedbackClicked() }
        about.setOnClickListener { presenter?.onAboutClicked() }
        share.setOnClickListener { presenter?.onShareClicked() }
        logout.setOnClickListener { presenter?.onLogoutClicked() }
    }

    override fun showUserDetails(user: User?) {
        if (user == null) return
        userName.text = user.name
        userDesc.text = user.phoneNumber
        if (user.imageUrl?.isNotEmpty() == true) {
            Glide.with(context!!).load(user.imageUrl).placeholder(R.drawable.ic_icon_user)
                .into(userImage)
            userImage.shouldShow = true
            userImageText.shouldShow = false
        } else if (user.name?.isNotEmpty() == true) {
            userImageText.text = user.name?.substring(0, 1)
            userImage.shouldShow = false
            userImageText.shouldShow = true
        }
    }

    override fun showShareIntent() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is a message from SpiffyShow app")
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app)))
    }

    override fun showToast(message: String) {
        activity?.toast(message)
    }

    override fun stopService() {
        val intent = Intent(activity, AppService::class.java)
        intent.action = AppService.ACTION_STOP_FOREGROUND_SERVICE
        activity?.stopService(intent)
    }

    override fun closeDrawer() {
        (activity as MainActivity).closeDrawer()
    }

    override fun logoutFirebaseNavigateToLogin() {
        FirebaseAuth.getInstance().signOut()
        fragmentManager?.popBackStack()
        (activity as MainActivity).navigateToLogin()
    }

    override fun navigateToHome() {
        (activity as MainActivity).navigateToScreen(Navigation.HOME)
    }

    override fun navigateToAboutScreen() {
        (activity as MainActivity).navigateToScreen(Navigation.ABOUT)
    }

    override fun navigateToFeedback() {
        (activity as MainActivity).navigateToScreen(Navigation.FEEDBACK)
    }

    override fun initPresenter() = drawerPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_drawer
}