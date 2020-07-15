package com.geeklabs.spiffyshow.ui.components.main.search

import androidx.appcompat.widget.SearchView
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.extensions.setEmptyStateView
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.view_state_layout.*
import kotlinx.android.synthetic.main.view_state_layout.view.*
import javax.inject.Inject

class SearchFragment : BaseFragment<SearchContract.View, SearchContract.Presenter>(),
    SearchContract.View {

    @Inject
    lateinit var searchPresenter: SearchPresenter
    private lateinit var adapter: SearchAdapter

    override fun initUI() {
        adapter = SearchAdapter()
        adapter.setEmptyStateView(stateLayout)
        recyclerViewSearch.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter?.onSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                presenter?.onSearch(newText)
                return false
            }
        })
    }

    override fun setState(progress: Boolean, empty: Boolean, error: Boolean) {
        stateLayout.stateProgress.visible = progress
        stateLayout.progressText.visible = progress
        stateLayout.stateEmpty.visible = empty
        stateLayout.stateError.visible = error
        stateLayout.stateEmpty.emptyTitle.text = getString(R.string.no_items)
        stateLayout.stateEmpty.emptyDescription.visible = false
    }

    override fun showItems(list: MutableList<String>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
        if (list.size == 0) {
            setState(empty = true)
        }
    }

    override fun initPresenter() = searchPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_search
}