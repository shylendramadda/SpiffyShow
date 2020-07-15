package com.geeklabs.spiffyshow.ui.components.main.original

import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.local.item.DeleteItemLocalUseCase
import com.geeklabs.spiffyshow.domain.local.item.FetchItemsFromLocalUseCase
import com.geeklabs.spiffyshow.extensions.applySchedulers
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.log4k.e
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class OriginalPresenter @Inject constructor(
    private val applicationState: ApplicationState,
    private val fetchItemsFromLocalUseCase: FetchItemsFromLocalUseCase,
    private val deleteItemLocalUseCase: DeleteItemLocalUseCase
) : BasePresenter<OriginalContract.View>(),
    OriginalContract.Presenter {

    private var items = mutableListOf<Item>()
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
            fetchItemsFromLocalUseCase.execute(Unit)
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
                }, {
                    getView()?.setState(error = true)
                    e("Error getItemsFromLocal: ${it.message}")
                    getView()?.showToast("Error getItemsFromLocal: ${it.message}")
                })
        )
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

    override fun onEditClicked(
        item: Item,
        isTrim: Boolean
    ) {
        getView()?.navigateToTrim(item, isTrim)
    }

    override fun onDeleteClicked(item: Item) {
        disposables?.add(Observable.fromCallable {
            deleteItemLocalUseCase.execute(item.id)
        }.subscribeOn(Schedulers.io()).subscribe({}, {
            e("Error deleteCategoryLocal: ${it.message}")
        }))
        getView()?.notifyAdapter()
    }

    override fun onProfileClicked(user: User) {
        getView()?.navigateToUserProfile(user)
    }

    override fun onCommentClicked(item: Item) {
        getView()?.navigateToComment(item)
    }
}