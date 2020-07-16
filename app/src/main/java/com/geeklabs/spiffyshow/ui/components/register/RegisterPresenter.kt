package com.geeklabs.spiffyshow.ui.components.register

import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.local.user.SaveUpdateUserUseCase
import com.geeklabs.spiffyshow.domain.remote.user.SaveUpdateUserRemoteUseCase
import com.geeklabs.spiffyshow.enums.StringEnum
import com.geeklabs.spiffyshow.extensions.applySchedulers
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.StringUtils
import com.log4k.d
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterPresenter @Inject constructor(
    private val saveUpdateUserRemoteUseCase: SaveUpdateUserRemoteUseCase,
    private val saveUpdateUserUseCase: SaveUpdateUserUseCase,
    private val stringUtils: StringUtils
) :
    BasePresenter<RegisterContract.View>(),
    RegisterContract.Presenter {

    private var user: User? = null

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
    }

    override fun onRegisterButtonClicked(user: User) {
        if (this.user != null) {
            user.pinCode = this.user?.pinCode ?: ""
            user.city = this.user?.city ?: ""
            user.state = this.user?.state ?: ""
            user.addressInfo = this.user?.addressInfo ?: ""
        }
        when {
            user.name?.trim()?.isEmpty() == true -> {
                getView()?.showToast("Please enter your name")
            }
            user.name?.trim()?.isNotEmpty() == true &&
                    user.name?.trim()?.length ?: 0 < 3 -> {
                getView()?.showToast("Your name should be minimum of 3 characters")
            }
            user.phoneNumber?.trim()?.isEmpty() == true -> {
                getView()?.showToast("Please enter your mobile number")
            }
            user.phoneNumber?.trim()?.isNotEmpty() == true &&
                    user.phoneNumber?.trim()?.length ?: 0 < 10 -> {
                getView()?.showToast("Your mobile number should be 10 digits")
            }
            user.pinCode?.trim()?.isEmpty() == true -> {
                getView()?.showToast("Please enter your pincode")
            }
            user.pinCode?.trim()?.isNotEmpty() == true && user.pinCode?.trim()?.length ?: 0 < 6 -> {
                getView()?.showToast("Your pincode should be 6 digits")
            }
            else -> {
//                registerUser(user)
            }
        }
    }

    private fun registerUser(user: User) {
        disposables?.add(
            saveUpdateUserRemoteUseCase.execute(user)
                .applySchedulers()
                .subscribe({
                    if (it != null) {
                        saveUserInLocal(it)
                    }
                    getView()?.showToast(stringUtils.getString(StringEnum.UPDATE_SUCCESS.resId))
                }, {
                    d("registerUser: ${it.message}")
                })
        )
    }

    private fun saveUserInLocal(user: User) {
        Observable.fromCallable {
            saveUpdateUserUseCase.execute(user)
        }.subscribeOn(Schedulers.newThread()).subscribe()
        getView()?.showToast("Registered successfully")
        getView()?.navigateToLogin()
    }
}