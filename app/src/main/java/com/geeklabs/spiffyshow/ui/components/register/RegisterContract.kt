package com.geeklabs.spiffyshow.ui.components.register

import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface RegisterContract {

    interface View : BaseContract.View {
        fun initUI()
        fun showToast(message: String)
        fun navigateToLogin()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onRegisterButtonClicked(user: User)
    }
}