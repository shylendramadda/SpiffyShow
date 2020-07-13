package com.geeklabs.spiffyshow.ui.components.notifications

import com.geeklabs.spiffyshow.data.local.models.notification.Notification
import com.geeklabs.spiffyshow.ui.base.BaseContract

interface NotificationContract {
    interface View : BaseContract.View {
        fun initUI()
        fun setState(progress: Boolean = false, empty: Boolean = false, error: Boolean = false)
        fun showItems(list: MutableList<Notification>)
    }

    interface Presenter : BaseContract.Presenter<View> {
    }
}