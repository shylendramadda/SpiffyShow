package com.geeklabs.spiffyshow.ui.components.comment

import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Comment
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.extensions.onTextChanged
import com.geeklabs.spiffyshow.extensions.setEmptyStateView
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_comment.*
import kotlinx.android.synthetic.main.view_state_layout.*
import kotlinx.android.synthetic.main.view_state_layout.view.*
import javax.inject.Inject

class CommentFragment : BaseFragment<CommentContract.View, CommentContract.Presenter>(),
    CommentContract.View {

    @Inject
    lateinit var commentPresenter: CommentPresenter
    private lateinit var adapter: CommentAdapter
    private var obj: Any? = null

    override fun initUI() {
        if (obj != null) {
            if (obj is Trim) {
                val trim = obj as Trim
            } else {
                val item = obj as Original
            }
        }
        adapter = CommentAdapter()
        adapter.setEmptyStateView(stateLayout)
        recyclerViewComments.adapter = adapter

        commentET.onTextChanged {
            if (it.isNotEmpty()) {
                postTV.isEnabled = true
                postTV.setTextColor(context!!.getColor(R.color.blueTertiary))
            } else {
                postTV.setTextColor(context!!.getColor(R.color.grayPrimary))
                postTV.isEnabled = false
            }
        }
        postTV.setOnClickListener {
            presenter?.onPostCommentClicked(commentET.text.toString().trim())
            commentET.setText("")
        }
    }

    override fun setState(progress: Boolean, empty: Boolean, error: Boolean) {
        stateLayout.stateProgress.visible = progress
        stateLayout.progressText.visible = progress
        stateLayout.stateEmpty.visible = empty
        stateLayout.stateError.visible = error
        stateLayout.stateEmpty.emptyTitle.text = getString(R.string.no_notifications)
        stateLayout.stateEmpty.emptyDescription.visible = false
    }

    override fun showItems(list: MutableList<Comment>) {
        adapter.list = list
        adapter.notifyDataSetChanged()
    }

    override fun notifyAdapter() {
        adapter.notifyItemInserted(0)
        recyclerViewComments.smoothScrollToPosition(0)
    }

    override fun initPresenter() = commentPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_comment

    companion object {
        fun newInstance(obj: Any): CommentFragment {
            return CommentFragment().apply {
                this.obj = obj
            }
        }
    }
}