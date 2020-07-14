package com.geeklabs.spiffyshow.ui.components.main.home

import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.local.trim.DeleteTrimFromLocalUseCase
import com.geeklabs.spiffyshow.domain.local.trim.FetchTrimsFromLocalUseCase
import com.geeklabs.spiffyshow.extensions.applySchedulers
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.log4k.e
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val applicationState: ApplicationState,
    private val fetchTrimsFromLocalUseCase: FetchTrimsFromLocalUseCase,
    private val deleteTrimsFromLocalUseCase: DeleteTrimFromLocalUseCase
) : BasePresenter<HomeContract.View>(),
    HomeContract.Presenter {

    private var items = mutableListOf<Trim>()
    private lateinit var fileMetaData: FileMetaData
    private var user: User? = null

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
        loadItemsFromLocal()
        this.user = applicationState.user
    }

    private fun loadItemsFromLocal() {
        items.clear()
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
                    items = it
                    getView()?.showItems(it, user)
                    getView()?.setState(progress = false)
                }, {
                    getView()?.setState(error = true)
                    e("Error getItemsFromLocal: ${it.message}")
                    getView()?.showToast("Error getItemsFromLocal: ${it.message}")
                })
        )
    }

    override fun setFileMetaData(fileMetaData: FileMetaData) {
        this.fileMetaData = fileMetaData
    }

    override fun onSearch(query: String) {
        val inputQuery = query.toLowerCase(Locale.getDefault())
        val finalList = if (inputQuery.isEmpty()) items else items.filter {
            it.title.toLowerCase(Locale.getDefault()).contains(inputQuery, true) ||
                    it.description.contains(inputQuery, true) ||
                    it.category.contains(inputQuery, true)
        }
        finalList.sortedBy { it.title }
        getView()?.showItems(finalList.toMutableList(), user)
    }

    override fun onEditClicked(item: Trim) {
        getView()?.navigateToTrim(item)
    }

    override fun onDeleteClicked(item: Trim) {
        disposables?.add(Observable.fromCallable {
            deleteTrimsFromLocalUseCase.execute(item.id)
        }.subscribeOn(Schedulers.io()).subscribe({}, {
            e("Error deleteCategoryLocal: ${it.message}")
        }))
        getView()?.notifyAdapter()
    }

    override fun onShareClicked(item: Trim) {
        getView()?.startFileShareIntent(item)
    }

}