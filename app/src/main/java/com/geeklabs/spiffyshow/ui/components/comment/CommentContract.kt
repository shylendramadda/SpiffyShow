package com.geeklabs.spiffyshow.ui.components.comment

import com.geeklabs.spiffyshow.data.local.models.item.Comment
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface CommentContract {
    interface View : BaseContract.View {
        fun initUI()
        fun setState(progress: Boolean = false, empty: Boolean = false, error: Boolean = false)
        fun showItems(list: MutableList<Comment>)
        fun notifyAdapter()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onPostCommentClicked(text: String)
    }
}