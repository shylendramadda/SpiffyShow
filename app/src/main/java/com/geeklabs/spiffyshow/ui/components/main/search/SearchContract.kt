package com.geeklabs.spiffyshow.ui.components.main.search

import com.geeklabs.spiffyshow.ui.base.BaseContract

interface SearchContract {
    interface View : BaseContract.View {
        fun initUI()
        fun setState(progress: Boolean = false, empty: Boolean = false, error: Boolean = false)
        fun showItems(list: MutableList<String>)
        fun navigateToHome()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onSearch(query: String)
        fun onItemClicked(text: String)
    }
}