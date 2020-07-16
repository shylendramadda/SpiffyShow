package com.geeklabs.spiffyshow.ui.components.main

import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.local.file.ProcessFileUriUseCase
import com.geeklabs.spiffyshow.domain.local.location.FetchDeviceLocationUseCase
import com.geeklabs.spiffyshow.domain.local.user.FetchUserFromLocalUseCase
import com.geeklabs.spiffyshow.domain.local.user.SaveUpdateUserUseCase
import com.geeklabs.spiffyshow.domain.remote.user.SaveUpdateUserRemoteUseCase
import com.geeklabs.spiffyshow.enums.StringEnum
import com.geeklabs.spiffyshow.events.LocationUpdateEvent
import com.geeklabs.spiffyshow.extensions.applySchedulers
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.RxBus
import com.geeklabs.spiffyshow.utils.StringUtils
import com.log4k.e
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val rxBus: RxBus,
    private val stringUtils: StringUtils,
    private val applicationState: ApplicationState,
    private val saveUpdateUserUseCase: SaveUpdateUserUseCase,
    private val fetchUserFromLocalUseCase: FetchUserFromLocalUseCase,
    private val saveUpdateUserRemoteUseCase: SaveUpdateUserRemoteUseCase,
    private val fetchDeviceLocationUseCase: FetchDeviceLocationUseCase,
    private val processFileUriUseCase: ProcessFileUriUseCase
) : BasePresenter<MainContract.View>(), MainContract.Presenter {

    private var user: User? = null

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
        getUserFromLocal()
        listenToLocationUpdates()
    }

    private fun getUserFromLocal() {
        disposables?.add(
            fetchUserFromLocalUseCase.execute(Unit)
                .applySchedulers()
                .subscribe({
                    user = it
                    applicationState.user = it
                    applicationState.isAdmin = it.role.equals(Constants.ADMIN, false)
                    getView()?.navigateToHome()
                }, {
                    getView()?.showToast(stringUtils.getString(StringEnum.SOMETHING_WENT_WRONG.resId) + " Error: " + it.message)
                    e("loadUserFromLocal ${it.message}")
                })
        )
    }

    private fun listenToLocationUpdates() {
        disposables?.add(
            rxBus.listen(LocationUpdateEvent::class.java)
                .applySchedulers()
                .subscribe({
                    loadDeviceLocation()
                }, {
                    e("listenToLocationUpdates ${it.message}")
                })
        )
    }

    private fun loadDeviceLocation() {
        disposables?.add(
            fetchDeviceLocationUseCase.execute(Unit)
                .applySchedulers()
                .subscribe({
                    if (it != null) {
                        if (user == null) return@subscribe
                        user?.addressInfo = it.addressLine
                        user?.city = it.city
                        user?.state = it.state
                        user?.country = it.country
                        user?.pinCode = it.pinCode
                        user?.latitude = it.latitude
                        user?.longitude = it.longitude
                        saveUpdateUser(user!!)
                    }
                }, {
                    e("onMapLoaded ${it.message}")
                })
        )
    }

    override fun onMenuButtonClicked() {
        getView()?.showDrawer()
    }

    override fun onAddButtonClicked(isPermissionEnable: Boolean) {
        getView()?.showUploadAlert()
    }

    override fun onSaveFilePath(fileUri: String?) {
        if (fileUri == null) {
            getView()?.showToast(stringUtils.getString(StringEnum.FILE_PATH_ERROR.resId))
            return
        }
        disposables?.add(
            processFileUriUseCase.execute(fileUri)
                .applySchedulers()
                .doOnSubscribe { getView()?.showHideProgress(true) }
                .doFinally { getView()?.showHideProgress(false) }
                .subscribe({
                    if (it.second.isNotEmpty()) {
                        getView()?.showAlert(it.second)
                        return@subscribe
                    }
                    getView()?.navigateToTrim(Item(fileMetaData = it.first), false)
                }, {
                    getView()?.showAlert(
                        stringUtils.getString(StringEnum.FILE_PATH_ERROR.resId) + "${it.message}"
                    )
                    getView()?.showHideProgress(false)
                })
        )
    }

    private fun saveUpdateUser(user: User) {
        applicationState.user = user
        Observable.fromCallable {
            saveUpdateUserUseCase.execute(user)
        }.subscribeOn(Schedulers.newThread()).subscribe()
        getView()?.stopService()
        saveUpdateUserInRemote(user)
    }

    private fun saveUpdateUserInRemote(user: User) {
        disposables?.add(
            saveUpdateUserRemoteUseCase.execute(user)
                .applySchedulers()
                .subscribe({
                    if (it == null) {
                        getView()?.showToast(stringUtils.getString(StringEnum.SOMETHING_WENT_WRONG_SERVER.resId))
                    }
                }, {
                    e("Error: ${it.message}")
                })
        )
    }

    override fun onChooseFileClicked(isPermissionEnable: Boolean) {
        if (isPermissionEnable) {
            getView()?.startVideoIntent()
        } else {
            getView()?.askPermissions()
        }
    }

    override fun onAddFromLinkButton() {
        getView()?.navigateToTrim(Item(), false)
    }

    override fun onDestroyed() {
        getView()?.stopService()
        super.onDestroyed()
    }
}