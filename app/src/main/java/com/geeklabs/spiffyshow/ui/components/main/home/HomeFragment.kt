package com.geeklabs.spiffyshow.ui.components.main.home

import android.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.extensions.*
import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import com.geeklabs.spiffyshow.utils.Constants
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.view_state_layout.*
import kotlinx.android.synthetic.main.view_state_layout.view.*
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    @Inject
    lateinit var homePresenter: HomePresenter
    private lateinit var itemsAdapter: ItemsAdapter
    private var fileMetaData: FileMetaData? = null

    override fun initUI() {
        view?.hideKeyboard(context!!)
        if (fileMetaData != null) {
            presenter?.setFileMetaData(fileMetaData!!)
        }
        itemsAdapter = ItemsAdapter(
            { presenter?.onEditClicked(it) },
            { presenter?.onDeleteClicked(it) }
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

    override fun setState(progress: Boolean, empty: Boolean, error: Boolean) {
        stateLayout.stateProgress.visible = progress
        stateLayout.progressText.visible = progress
        stateLayout.stateEmpty.visible = empty
        stateLayout.stateError.visible = error
        stateLayout.stateEmpty.emptyTitle.text = getString(R.string.no_items)
        stateLayout.stateEmpty.emptyDescription.visible = false
    }

    override fun showItems(
        items: MutableList<Trim>,
        user: User?
    ) {
        itemsAdapter.user = user
        itemsAdapter.items = items
        itemsAdapter.notifyDataSetChanged()
        val isMoreThanOne = items.size > 1
        if (isMoreThanOne) recyclerViewItemList.smoothScrollToPosition(0)
    }

    override fun showMessage(message: Int) {
        val messageText = when (message) {
            3 -> getString(R.string.no_mobile_number)
            4 -> getString(R.string.enter_valid_mobile_number)
            else -> getString(R.string.alert)
        }
        showToast(messageText)
    }

    override fun showAlertDialog(message: Int) {
        val messageText =
            when (message) {
                1 -> getString(R.string.error_message)
                2 -> getString(R.string.no_network)
                5 -> getString(R.string.thanks_order_message)
                6 -> getString(R.string.order_time_message)
                7 -> getString(R.string.no_item_select_message)
                else -> getString(R.string.alert)
            }
        AlertDialog.Builder(context)
            .setMessage(messageText)
            .setPositiveButton(R.string.ok, null)
            .show()
    }

    override fun showToast(title: String) {
        activity?.toast(title)
    }

    override fun navigateToTrim(item: Trim) {
        (activity as MainActivity).navigateToTrim(item.fileMetaData)
    }

    override fun initPresenter() = homePresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_home

    companion object {
        fun newInstance(fileMetaData: FileMetaData): HomeFragment {
            return HomeFragment().apply {
                this.fileMetaData = fileMetaData
            }
        }
    }
}