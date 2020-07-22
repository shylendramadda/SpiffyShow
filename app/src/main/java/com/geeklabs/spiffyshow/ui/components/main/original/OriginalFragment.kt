package com.geeklabs.spiffyshow.ui.components.main.original

import androidx.appcompat.widget.SearchView
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.extensions.*
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import com.geeklabs.spiffyshow.utils.Constants
import com.log4k.e
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.view_state_layout.*
import kotlinx.android.synthetic.main.view_state_layout.view.*
import javax.inject.Inject

class OriginalFragment : BaseFragment<OriginalContract.View, OriginalContract.Presenter>(),
    OriginalContract.View {

    @Inject
    lateinit var originalPresenter: OriginalPresenter

    @Inject
    lateinit var applicationState: ApplicationState
    private lateinit var itemsAdapter: OriginalAdapter

    override fun initUI() {
        view?.hideKeyboard(context!!)
        itemsAdapter = OriginalAdapter(onEditClicked,
            { presenter?.onDeleteClicked(it) },
            { presenter?.onCommentClicked(it) },
            { presenter?.onProfileClicked(it) }
        )
        recyclerViewItemList.adapter = itemsAdapter
        itemsAdapter.setEmptyStateView(stateLayout)

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
        swipeRefreshLayout.setOnRefreshListener {
            withDelay(Constants.DELAY_REFRESH) {
                swipeRefreshLayout?.isRefreshing = false
            }
        }
    }

    private val onEditClicked = fun(
        original: Original,
        isTrim: Boolean
    ) {
        presenter?.onEditClicked(original, isTrim)
    }

    override fun setState(progress: Boolean, empty: Boolean, error: Boolean) {
        stateLayout.visible = true
        stateLayout.stateProgress.visible = progress
        stateLayout.progressText.visible = progress
        stateLayout.stateEmpty.visible = empty
        stateLayout.stateError.visible = error
        stateLayout.stateEmpty.emptyTitle.text = getString(R.string.no_items)
        stateLayout.stateEmpty.emptyDescription.visible = false
    }

    override fun showItems(
        originals: MutableList<Original>
    ) {
        itemsAdapter.items = originals
        itemsAdapter.user = applicationState.user
        itemsAdapter.notifyDataSetChanged()
    }

    override fun notifyItemDeleted(original: Original) {
        try {
            val itemIndex = itemsAdapter.items.indexOfFirst { it.id == original.id }
            if (itemIndex >= 0) {
                itemsAdapter.items.removeAt(itemIndex)
                itemsAdapter.notifyItemRemoved(itemIndex)
            }
        } catch (ex: IndexOutOfBoundsException) {
            e("itemRemoved failed", ex)
        }
    }

    override fun navigateToTrim(
        original: Original,
        isTrim: Boolean
    ) {
        (activity as MainActivity).navigateToTrim(original, isTrim)
    }

    override fun navigateToUserProfile(user: User) {
        (activity as MainActivity).navigateToUserProfile(user)
    }

    override fun navigateToComment(original: Original) {
        (activity as MainActivity).navigateToComment(original)
    }

    override fun showToast(title: String) {
        activity?.toast(title)
    }

    override fun initPresenter() = originalPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_original
}