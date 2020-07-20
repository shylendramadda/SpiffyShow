package com.geeklabs.spiffyshow.ui.components.profile

import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.local.original.FetchOriginalsFromLocalUseCase
import com.geeklabs.spiffyshow.domain.local.trim.FetchTrimsFromLocalUseCase
import com.geeklabs.spiffyshow.extensions.applySchedulers
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.log4k.e
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    private val applicationState: ApplicationState,
    private val fetchTrimsFromLocalUseCase: FetchTrimsFromLocalUseCase,
    private val fetchOriginalsFromLocalUseCase: FetchOriginalsFromLocalUseCase
) : BasePresenter<ProfileContract.View>(),
    ProfileContract.Presenter {

    private var trimItems = mutableListOf<Trim>()
    private var originalItems = mutableListOf<Original>()
    private var user: User? = null

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
        this.user = applicationState.user
        loadTrims()
        loadOriginals()
    }

    private fun loadOriginals() {
        disposables?.add(
            fetchOriginalsFromLocalUseCase.execute(Unit)
                .applySchedulers()
                .doOnSubscribe { getView()?.setState(progress = true) }
                .subscribe({
                    if (it.isEmpty()) {
                        getView()?.setState(empty = true)
                        return@subscribe
                    }
                    it.sortByDescending { item -> item.time }
                    originalItems = it
                    getView()?.showOriginalItems(it, user)
                    getView()?.setState(progress = false)
                }, {
                    getView()?.setState(error = true)
                    e("Error getItemsFromLocal: ${it.message}")
                    getView()?.showToast("Error getItemsFromLocal: ${it.message}")
                })
        )
    }

    private fun loadTrims() {
        disposables?.add(
            fetchTrimsFromLocalUseCase.execute(Unit)
                .applySchedulers()
                .doOnSubscribe { getView()?.setState(progress = true) }
                .subscribe({
                    if (it.isEmpty()) {
                        getView()?.setState(empty = true)
                        return@subscribe
                    }
                    it.sortByDescending { item -> item.time }
                    trimItems = it
                    getView()?.showTrimItems(it, user)
                    getView()?.setState(progress = false)
                }, {
                    getView()?.setState(error = true)
                    e("Error getItemsFromLocal: ${it.message}")
                    getView()?.showToast("Error getItemsFromLocal: ${it.message}")
                })
        )
    }

}