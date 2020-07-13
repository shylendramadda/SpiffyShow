package com.geeklabs.spiffyshow.ui.components.main.original

import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.local.item.DeleteItemLocalUseCase
import com.geeklabs.spiffyshow.domain.local.item.FetchItemsFromLocalUseCase
import com.geeklabs.spiffyshow.domain.local.item.SaveUpdateItemsInLocalUseCase
import com.geeklabs.spiffyshow.extensions.applySchedulers
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Utils
import com.log4k.e
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class OriginalPresenter @Inject constructor(
    private val applicationState: ApplicationState,
    private val fetchItemsFromLocalUseCase: FetchItemsFromLocalUseCase,
    private val saveUpdateItemsInLocalUseCase: SaveUpdateItemsInLocalUseCase,
    private val deleteItemLocalUseCase: DeleteItemLocalUseCase
) : BasePresenter<OriginalContract.View>(),
    OriginalContract.Presenter {

    private var items = mutableListOf<Item>()
    private lateinit var fileMetaData: FileMetaData
    private var user: User? = null

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
        loadItemsFromLocal()
        this.user = applicationState.user
    }

    override fun setFileMetaData(fileMetaData: FileMetaData) {
        this.fileMetaData = fileMetaData
        val item = Item(
            userId = applicationState.user?.id ?: 0,
            time = Utils.getCurrentTime(),
            fileMetaData = fileMetaData
        )
        Observable.fromCallable {
            saveUpdateItemsInLocalUseCase.execute(mutableListOf(item))
        }.subscribeOn(Schedulers.newThread()).subscribe()
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
                    it.sortBy { item -> item.title }
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

    override fun onEditClicked(item: Item) {
        getView()?.navigateToTrim(item)
    }

    override fun onDeleteClicked(item: Item) {
        disposables?.add(Observable.fromCallable {
            deleteItemLocalUseCase.execute(item.id)
        }.subscribeOn(Schedulers.io()).subscribe({}, {
            e("Error deleteCategoryLocal: ${it.message}")
        }))
    }
}