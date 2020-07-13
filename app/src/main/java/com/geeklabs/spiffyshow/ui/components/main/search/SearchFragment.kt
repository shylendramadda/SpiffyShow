package com.geeklabs.spiffyshow.ui.components.main.search

import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import javax.inject.Inject

class SearchFragment : BaseFragment<SearchContract.View, SearchContract.Presenter>(),
    SearchContract.View {

    @Inject
    lateinit var searchPresenter: SearchPresenter

    override fun initUI() {

    }

    override fun initPresenter() = searchPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_search

}