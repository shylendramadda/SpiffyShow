package com.geeklabs.spiffyshow.ui.components.main.search

import com.geeklabs.spiffyshow.ui.base.BasePresenter
import javax.inject.Inject

class SearchPresenter @Inject constructor() : BasePresenter<SearchContract.View>(),
    SearchContract.Presenter {

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
    }
}