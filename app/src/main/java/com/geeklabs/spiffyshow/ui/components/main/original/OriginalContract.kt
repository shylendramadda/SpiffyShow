package com.geeklabs.spiffyshow.ui.components.main.original

import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface OriginalContract {
    interface View : BaseContract.View {
        fun initUI()
        fun setState(progress: Boolean = false, empty: Boolean = false, error: Boolean = false)
        fun showItems(originals: MutableList<Original>)

        fun showToast(title: String)
        fun navigateToTrim(
            original: Original,
            isTrim: Boolean
        )

        fun navigateToUserProfile(user: User)
        fun navigateToComment(original: Original)
        fun notifyItemDeleted(original: Original)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onSearch(query: String)
        fun onEditClicked(
            original: Original,
            isTrim: Boolean
        )

        fun onDeleteClicked(original: Original)
        fun onProfileClicked(user: User)
        fun onCommentClicked(original: Original)
    }
}