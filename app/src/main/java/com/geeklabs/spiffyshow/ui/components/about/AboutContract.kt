package com.geeklabs.spiffyshow.ui.components.about

import com.geeklabs.spiffyshow.ui.base.BaseContract

interface AboutContract {

    interface View : BaseContract.View {
        fun initUI()
    }

    interface Presenter : BaseContract.Presenter<View> {

    }
}