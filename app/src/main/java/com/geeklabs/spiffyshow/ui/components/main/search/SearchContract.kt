package com.geeklabs.spiffyshow.ui.components.main.search

import com.geeklabs.spiffyshow.ui.base.BaseContract

interface SearchContract {
    interface View : BaseContract.View {
        fun initUI()
    }

    interface Presenter : BaseContract.Presenter<View> {
    }
}