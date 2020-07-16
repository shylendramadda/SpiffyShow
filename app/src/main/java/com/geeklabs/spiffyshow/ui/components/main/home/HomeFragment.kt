package com.geeklabs.spiffyshow.ui.components.main.home

import android.content.Intent
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
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
import java.io.File
import javax.inject.Inject


class HomeFragment : BaseFragment<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    @Inject
    lateinit var homePresenter: HomePresenter
    private lateinit var trimAdapter: TrimAdapter
    private var fileMetaData: FileMetaData? = null

    override fun initUI() {
        view?.hideKeyboard(context!!)
        if (fileMetaData != null) {
            presenter?.setFileMetaData(fileMetaData!!)
        }
        trimAdapter = TrimAdapter(
            { presenter?.onEditClicked(it) },
            { presenter?.onDeleteClicked(it) },
            { presenter?.onCommentClicked(it) },
            { presenter?.onShareClicked(it) },
            { presenter?.onProfileClicked(it) }
        )
        recyclerViewItemList.adapter = trimAdapter
        trimAdapter.setEmptyStateView(stateLayout)

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
        trimAdapter.user = user
        trimAdapter.items = items
        trimAdapter.notifyDataSetChanged()
        val isMoreThanOne = items.size > 1
        if (isMoreThanOne) recyclerViewItemList.smoothScrollToPosition(0)
        if (items.size == 0) setState(empty = true)
    }

    override fun showMessage(message: Int) {
        val messageText = when (message) {
            3 -> getString(R.string.no_mobile_number)
            4 -> getString(R.string.enter_valid_mobile_number)
            else -> getString(R.string.alert)
        }
        showToast(messageText)
    }

    override fun showAlertDialog(message: String) {
        alert(message) {}.show()
    }

    override fun startFileShareIntent(item: Trim) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = FILE_TYPE
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Sharing File '${item.title}' from SpiffyShow(www.spiffyshow.com)"
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "Sharing File '${item.title}' from SpiffyShow(www.spiffyshow.com)"
            )
            val fileURI = FileProvider.getUriForFile(
                context!!, context!!.packageName + ".provider",
                File(item.fileMetaData?.path ?: "")
            )
            putExtra(Intent.EXTRA_STREAM, fileURI)
        }
        startActivity(shareIntent)
    }

    override fun notifyAdapter() {
//        trimAdapter.notifyDataSetChanged()
    }

    override fun showToast(title: String) {
        activity?.toast(title)
    }

    override fun navigateToTrim(item: Trim) {
        (activity as MainActivity).navigateToTrim(item, false)
    }

    override fun navigateToUserProfile(user: User) {
        (activity as MainActivity).navigateToUserProfile(user)
    }

    override fun navigateToComment(item: Trim) {
        (activity as MainActivity).navigateToComment(item)
    }

    override fun onResume() {
        super.onResume()
        view?.hideKeyboard(context!!)
    }

    override fun initPresenter() = homePresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_home

    companion object {
        private const val FILE_TYPE = "video/*"
    }
}