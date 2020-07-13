package com.geeklabs.spiffyshow.ui.components.notifications

import com.geeklabs.spiffyshow.data.local.models.notification.Notification
import com.geeklabs.spiffyshow.ui.base.BasePresenter
import com.geeklabs.spiffyshow.utils.Utils
import javax.inject.Inject

class NotificationPresenter @Inject constructor() : BasePresenter<NotificationContract.View>(),
    NotificationContract.Presenter {

    val list = mutableListOf<Notification>()

    override fun onCreated() {
        super.onCreated()
        getView()?.initUI()
        loadNotifications()
    }

    private fun loadNotifications() {
        for (i in 1..20) {
            val notification = Notification(
                title = "Got a video from spiffy show $i",
                desc = "This is sample description about the notification",
                time = Utils.getCurrentTime()
            )
            list.add(notification)
        }
        getView()?.showItems(list)
    }
}