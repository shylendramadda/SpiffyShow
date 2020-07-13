package com.geeklabs.spiffyshow.ui.components.about

import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import javax.inject.Inject

class AboutFragment : BaseFragment<AboutContract.View, AboutContract.Presenter>(),
    AboutContract.View {

    @Inject
    lateinit var aboutPresenter: AboutPresenter

    override fun initUI() {
    }

    override fun initPresenter() = aboutPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_about
}