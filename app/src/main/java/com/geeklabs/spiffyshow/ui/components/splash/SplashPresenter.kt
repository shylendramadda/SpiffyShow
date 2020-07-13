package com.geeklabs.spiffyshow.ui.components.splash

import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.local.user.SaveUpdateUserUseCase
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.PrefManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashPresenter @Inject constructor(
    private val prefManager: PrefManager,
    private val saveUpdateUserUseCase: SaveUpdateUserUseCase,
    private val applicationState: ApplicationState
) : BasePresenter<SplashContract.View>(),
    SplashContract.Presenter {

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
    }

    override fun onDelayFinished() {
        val isWelcomeLoaded = prefManager.getBoolean(Constants.IS_WELCOME_SEEN, false)
        val isLogin =
            prefManager.getBoolean(Constants.IS_LOGIN, false) // For testing set value as true
        when {
            isLogin -> getView()?.navigateToMainScreen()
            !isNetworkConnected() -> getView()?.showNoInternetSnackBar()
            !isWelcomeLoaded -> getView()?.navigateToWelcomeScreen()
            else -> getView()?.navigateToLoginScreen()
        }
        if (isNetworkConnected() || isLogin) {
            getView()?.finishView()
        }
    }

    private fun saveUserInLocal(user: User) {
        applicationState.user = user
        prefManager.save(Constants.IS_LOGIN, true)
        prefManager.save(Constants.USER_ID, user.id.toString())
        Observable.fromCallable {
            saveUpdateUserUseCase.execute(user)
        }.subscribeOn(Schedulers.newThread()).subscribe()
    }
}