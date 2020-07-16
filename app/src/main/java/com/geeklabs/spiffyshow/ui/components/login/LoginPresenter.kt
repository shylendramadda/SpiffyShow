package com.geeklabs.spiffyshow.ui.components.login

import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.local.user.LoginUseCase
import com.geeklabs.spiffyshow.domain.local.user.SaveUpdateUserUseCase
import com.geeklabs.spiffyshow.enums.StringEnum
import com.geeklabs.spiffyshow.extensions.applySchedulers
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.PrefManager
import com.geeklabs.spiffyshow.utils.StringUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.log4k.d
import com.log4k.w
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val stringUtils: StringUtils,
    private val prefManager: PrefManager,
    private val applicationState: ApplicationState,
    private val loginUseCase: LoginUseCase,
    private val saveUpdateUserUseCase: SaveUpdateUserUseCase
) :
    BasePresenter<LoginContract.View>(),
    LoginContract.Presenter {

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
    }

    override fun onVerifyButtonClicked(mobileNumber: String) {
        if (mobileNumber.isEmpty() || mobileNumber.length != 10) {
            getView()?.showToast("Please enter 10 digit mobile number")
        } else if (!isNetworkConnected()) {
            getView()?.showToast("Please check your network and try again")
        } else {
            getView()?.showProgress()
            getView()?.sendOTPViaFirebase("+91 $mobileNumber") //Supports only for India
        }
    }

    override fun onVerifyOTPClicked(otp: String, mobileNumber: String?) {
        when {
            otp.isEmpty() -> {
                getView()?.showToast("Please enter OTP")
            }
            otp.trim().length < 6 -> {
                getView()?.showToast("Please enter 6 digits OTP")
            }
            else -> {
                getView()?.showProgress()
                getView()?.verifyOTP(otp)
            }
        }
    }

    override fun onCompleteVerification(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
            d("onCompleteVerification: success")
            val firebaseUser = task.result?.user
            if (firebaseUser != null) {
                var phoneNumber = firebaseUser.phoneNumber?.trim() ?: return
                if (phoneNumber.contains(" ")) {
                    phoneNumber = phoneNumber.replace("\\s".toRegex(), "")
                }
                if (phoneNumber.startsWith("+91")) {
                    phoneNumber = phoneNumber.replace("\\+91".toRegex(), "")
                }
//                loginUser(phoneNumber) // Just for local Testing TODO
                saveUserInLocal(User(id = System.currentTimeMillis(), phoneNumber = phoneNumber))
            } else {
                getView()?.showToast(stringUtils.getString(StringEnum.INVALID_CODE.resId))
            }
        } else {
            w("onCompleteVerification: failure ${task.exception}")
            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                getView()?.showToast(stringUtils.getString(StringEnum.INVALID_CODE.resId))
            } else {
                getView()?.showToast("The verification failed due to: ${task.exception}")
            }
            getView()?.hideProgress()
        }
    }

    private fun loginUser(phoneNumber: String) {
        disposables?.add(
            loginUseCase.execute(phoneNumber)
                .applySchedulers()
                .subscribe({
                    if (it != null) {
                        saveUserInLocal(it)
                    } else {
                        getView()?.hideProgress()
                        getView()?.showToast(stringUtils.getString(StringEnum.SOMETHING_WENT_WRONG_SERVER.resId))
                    }
                }, {
                    getView()?.hideProgress()
                    getView()?.showToast(stringUtils.getString(StringEnum.SOMETHING_WENT_WRONG.resId))
                })
        )
    }

    private fun saveUserInLocal(user: User) {
        Observable.fromCallable {
            saveUpdateUserUseCase.execute(user)
        }.subscribeOn(Schedulers.newThread()).subscribe()
        applicationState.user = user
        prefManager.save(Constants.IS_LOGIN, true)
        prefManager.save(Constants.USER_ID, user.id.toString())
        getView()?.hideProgress()
        getView()?.showToast(stringUtils.getString(StringEnum.WELCOME.resId))
        getView()?.navigateToMainScreen()
    }

    override fun onChangeNumberClicked() {
        getView()?.showLoginView()
    }
}