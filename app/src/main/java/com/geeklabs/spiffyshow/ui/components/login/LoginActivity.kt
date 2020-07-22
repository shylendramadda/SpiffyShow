package com.geeklabs.spiffyshow.ui.components.login

import android.annotation.SuppressLint
import android.text.method.LinkMovementMethod
import androidx.work.WorkManager
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.extensions.input
import com.geeklabs.spiffyshow.extensions.launchActivity
import com.geeklabs.spiffyshow.extensions.toast
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.ui.base.BaseActivity
import com.geeklabs.spiffyshow.ui.common.Progress
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.PrefManager
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.log4k.d
import com.log4k.w
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.registerButton
import kotlinx.android.synthetic.main.layout_verify_phone.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class LoginActivity : BaseActivity<LoginContract.View, LoginContract.Presenter>(),
    LoginContract.View {

    @Inject
    lateinit var prefManager: PrefManager

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var loginPresenter: LoginPresenter
    private var storedVerificationId: String = ""
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private var mobileNumber: String? = ""
    private lateinit var progress: Progress

    override fun initUI() {
        phoneNumberET.requestFocus()
        progress = Progress(this, R.string.please_wait, false)
        firebaseAuth = FirebaseAuth.getInstance()
        termsCB.movementMethod = LinkMovementMethod.getInstance()
        sendOtpButton.setOnClickListener {
            mobileNumber = phoneNumberET.input()
            presenter?.onVerifyButtonClicked(mobileNumber ?: "", termsCB.isChecked)
        }
        registerButton.setOnClickListener {
            navigateToRegisterScreen()
        }
        verifyOTPButton.setOnClickListener {
            val otp = otpET.input().trim()
            presenter?.onVerifyOTPClicked(otp, mobileNumber)
        }
        changeNumberButton.setOnClickListener {
            phoneNumberET.setText("")
            otpET.setText("")
            presenter?.onChangeNumberClicked()
        }
        resendButton.setOnClickListener {
            mobileNumber = phoneNumberET.input()
            presenter?.onVerifyButtonClicked(mobileNumber ?: "", termsCB.isChecked)
        }
    }

    override fun sendOTPViaFirebase(mobileNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mobileNumber, Constants.SMS_TIME_OUT, TimeUnit.SECONDS, this, callbacks
        )
    }

    private val callbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                /*This callback will be invoked in two situations:
                1 - Instant verification. In some cases the phone number can be instantly
                    verified without needing to send or enter a verification code.
                2 - Auto-retrieval. On some devices Google Play services can automatically
                    detect the incoming verification SMS and perform verification without
                    user action.*/
                d("onVerificationCompleted smsCode: ${credential.smsCode}")
                if (credential.smsCode != null) {
                    otpET.setText(credential.smsCode)
                }
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                /*This callback is invoked in an invalid request for verification is made,
                for instance if the the phone number format is not valid.*/
                w("onVerificationFailed", exception)
                when (exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        showToast("Invalid request")
                    }
                    is FirebaseTooManyRequestsException -> {
                        showToast("The SMS quota for the project has been exceeded")
                    }
                    else -> {
                        showToast("VerificationFailed ${exception.message}")
                    }
                }
                //update the UI
                showLoginView()
                hideProgress()
            }

            @SuppressLint("SetTextI18n")
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                d("onCodeSent: $verificationId")
                otpSentTV.text = "${getString(R.string.otp_sent_to)} $mobileNumber"
                storedVerificationId = verificationId
                resendToken = token
                showVerifyOTPView()
                hideProgress()
            }
        }

    override fun verifyOTP(otp: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                presenter?.onCompleteVerification(it)
            }
    }

    private fun showVerifyOTPView() {
        loginLL.visible = false
        verifyLL.visible = true
    }

    override fun showLoginView() {
        otpSentTV.text = ""
        loginLL.visible = true
        verifyLL.visible = false
    }

    override fun navigateToMainScreen() {
        launchActivity<MainActivity> { }
        finish()
    }

    private fun navigateToRegisterScreen() {
//        launchActivity<RegisterActivity> { }
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun showProgress() {
        progress.show()
    }

    override fun hideProgress() {
        progress.dismiss()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun initPresenter() = loginPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.activity_login
}