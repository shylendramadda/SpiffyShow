package com.geeklabs.spiffyshow.ui.components.about

import com.geeklabs.spiffyshow.ui.base.BasePresenter
import javax.inject.Inject

class AboutPresenter @Inject constructor() : BasePresenter<AboutContract.View>(),
    AboutContract.Presenter {

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
    }
}