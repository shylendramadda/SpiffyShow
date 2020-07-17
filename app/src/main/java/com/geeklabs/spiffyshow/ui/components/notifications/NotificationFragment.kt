package com.geeklabs.spiffyshow.ui.components.notifications

import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.notification.Notification
import com.geeklabs.spiffyshow.extensions.setEmptyStateView
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.view_state_layout.*
import kotlinx.android.synthetic.main.view_state_layout.view.*
import javax.inject.Inject

class NotificationFragment :
    BaseFragment<NotificationContract.View, NotificationContract.Presenter>(),
    NotificationContract.View {

    @Inject
    lateinit var notificationPresenter: NotificationPresenter
    private lateinit var adapter: NotificationAdapter

    override fun initUI() {
        adapter = NotificationAdapter {
            presenter?.onDeleteClicked(it)
        }
        adapter.setEmptyStateView(stateLayout)
        recyclerViewNotifications.adapter = adapter
    }

    override fun setState(progress: Boolean, empty: Boolean, error: Boolean) {
        stateLayout.stateProgress.visible = progress
        stateLayout.progressText.visible = progress
        stateLayout.stateEmpty.visible = empty
        stateLayout.stateError.visible = error
        stateLayout.stateEmpty.emptyTitle.text = getString(R.string.no_notifications)
        stateLayout.stateEmpty.emptyDescription.visible = false
    }

    override fun showItems(list: MutableList<Notification>) {
        adapter.list = list
        adapter.notifyDataSetChanged()
    }

    override fun notifyItemRemoved(position: Int) {
        adapter.notifyItemRemoved(position)
    }

    override fun initPresenter() = notificationPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_notification
}