package com.geeklabs.spiffyshow.ui.components.register

import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.extensions.input
import com.geeklabs.spiffyshow.extensions.launchActivity
import com.geeklabs.spiffyshow.extensions.toast
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.ui.base.BaseActivity
import com.geeklabs.spiffyshow.ui.components.login.LoginActivity
import kotlinx.android.synthetic.main.layout_content_settings.*
import kotlinx.android.synthetic.main.toolbar_main.*
import javax.inject.Inject

class RegisterActivity : BaseActivity<RegisterContract.View, RegisterContract.Presenter>(),
    RegisterContract.View {

    @Inject
    lateinit var registerPresenter: RegisterPresenter

    override fun initUI() {
        menuButton.visible = false
        titleToolbar.text = getString(R.string.registration)
        saveButton.text = getString(R.string.register)
        saveButton.setOnClickListener {
            val name = nameET.input().trim()
            val mobile = phoneNumberET.input().trim()
            val email = emailET.input().trim()
            val address = addressET.input().trim()
            val interests = interestET.input().trim()
            val bio = bioET.input().trim()
            val user = User(
                name = name, phoneNumber = mobile, email = email,
                addressInfo = address, interests = interests, bio = bio
            )
            presenter?.onRegisterButtonClicked(user)
        }
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun navigateToLogin() {
        launchActivity<LoginActivity> { }
        finishView()
    }

    private fun finishView() {
        finish()
    }

    override fun initPresenter() = registerPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.activity_register

}