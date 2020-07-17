package com.geeklabs.spiffyshow.ui.components.notifications

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.notification.Notification
import com.geeklabs.spiffyshow.extensions.inflate
import com.geeklabs.spiffyshow.utils.Utils.getTimeAgo
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter(
    private val onDeleteClicked: (Int) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    var list = mutableListOf<Notification>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = parent.inflate(R.layout.item_notification)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(notification: Notification) = with(itemView) {
            titleTV.text = notification.title
            descriptionTV.text = notification.desc
            dateTV.text = getTimeAgo(notification.time)
            deleteIV.setOnClickListener {
                onDeleteClicked(adapterPosition)
            }
        }
    }
}