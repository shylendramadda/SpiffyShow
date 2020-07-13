package com.geeklabs.spiffyshow.ui.components.login

import com.geeklabs.spiffyshow.ui.base.BaseContract
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface LoginContract {
    interface View : BaseContract.View {
        fun initUI()
        fun showToast(message: String)
        fun sendOTPViaFirebase(mobileNumber: String)
        fun showLoginView()
        fun verifyOTP(otp: String)
        fun navigateToMainScreen()
        fun showProgress()
        fun hideProgress()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onVerifyButtonClicked(mobileNumber: String)
        fun onVerifyOTPClicked(otp: String, mobileNumber: String?)
        fun onChangeNumberClicked()
        fun onCompleteVerification(task: Task<AuthResult>)
    }
}
