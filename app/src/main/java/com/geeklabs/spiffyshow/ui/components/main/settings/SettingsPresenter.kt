package com.geeklabs.spiffyshow.ui.components.main.settings

import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.local.user.FetchUserFromLocalUseCase
import com.geeklabs.spiffyshow.domain.local.user.SaveUpdateUserUseCase
import com.geeklabs.spiffyshow.domain.remote.user.SaveUpdateUserRemoteUseCase
import com.geeklabs.spiffyshow.enums.StringEnum
import com.geeklabs.spiffyshow.extensions.applySchedulers
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.FileUtil
import com.geeklabs.spiffyshow.utils.StringUtil
import com.log4k.e
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SettingsPresenter @Inject constructor(
    private val stringUtil: StringUtil,
    private val applicationState: ApplicationState,
    private val fetchUserFromLocalUseCase: FetchUserFromLocalUseCase,
    private val saveUpdateUserUseCase: SaveUpdateUserUseCase,
    private val saveUpdateUserRemoteUseCase: SaveUpdateUserRemoteUseCase,
    private val fileUtil: FileUtil
) : BasePresenter<SettingsContract.View>(),
    SettingsContract.Presenter {

    private var isImageUpdate: Boolean = false
    private lateinit var user: User

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
        loadUserFromLocal()
//        user = User() // Just for testing
    }

    private fun loadUserFromLocal() {
        disposables?.add(
            fetchUserFromLocalUseCase.execute(Unit)
                .applySchedulers()
                .subscribe({
                    if (it != null) {
                        user = it
                        getView()?.showUserDetails(it)
                    }
                }, {
                    getView()?.showToast(stringUtil.getString(StringEnum.SOMETHING_WENT_WRONG.resId) + " Error: " + it.message)
                    e("loadUserFromLocal ${it.message}")
                })
        )
    }

    override fun onEditImageClicked(permissionEnable: Boolean) {
        if (!permissionEnable) {
            getView()?.askPermissions()
        } else {
            getView()?.showUploadImageDialog()
        }
    }

    override fun onSaveFilePath(imagePath: String) {
        isImageUpdate = true
        val fileMetaData = fileUtil.getFileMetaData(imagePath)
        if (fileMetaData == null || (fileMetaData.path.isEmpty() && fileMetaData.uri.isEmpty())) {
            getView()?.showToast(stringUtil.getString(StringEnum.FILE_PATH_ERROR.resId))
            return
        }
        if (fileMetaData.path.isEmpty()) {
            fileMetaData.path = fileMetaData.uri
        }
        user.imageUrl = fileMetaData.path
        getView()?.updateProfileImage(fileMetaData)
        saveUpdateUser(user)
    }

    override fun saveUpdateUser(user: User) {
        user.id = this.user.id
        applicationState.user = user
        Observable.fromCallable {
            saveUpdateUserUseCase.execute(user)
        }.subscribeOn(Schedulers.newThread()).subscribe()
//        saveUpdateUserInRemote(user)
        getView()?.showToast(stringUtil.getString(StringEnum.UPDATE_SUCCESS.resId))
        // Just for dev
        if (!isImageUpdate) {
            getView()?.navigateToHome()
        }
    }

    private fun saveUpdateUserInRemote(user: User) {
        disposables?.add(
            saveUpdateUserRemoteUseCase.execute(user)
                .applySchedulers()
                .subscribe({
                    if (it == null) {
                        getView()?.showToast(stringUtil.getString(StringEnum.SOMETHING_WENT_WRONG_SERVER.resId))
                    } else if (!isImageUpdate) {
                        getView()?.navigateToHome()
                    }
                }, {
                    e("Error: ${it.message}")
                })
        )
    }
}