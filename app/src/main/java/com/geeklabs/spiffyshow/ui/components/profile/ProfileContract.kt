package com.geeklabs.spiffyshow.ui.components.profile

import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface ProfileContract {
    interface View : BaseContract.View {
        fun initUI()
        fun showToast(message: String)
        fun setState(progress: Boolean = false, empty: Boolean = false, error: Boolean = false)
        fun showTrimItems(
            items: MutableList<Trim>,
            user: User?
        )

        fun showOriginalItems(
            items: MutableList<Item>,
            user: User?
        )
    }

    interface Presenter : BaseContract.Presenter<View> {
    }
}