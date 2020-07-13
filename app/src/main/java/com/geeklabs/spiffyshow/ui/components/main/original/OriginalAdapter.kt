package com.geeklabs.spiffyshow.ui.components.main.original

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.extensions.inflate
import com.geeklabs.spiffyshow.extensions.shouldShow
import com.geeklabs.spiffyshow.utils.Utils.getTimeAgo
import kotlinx.android.synthetic.main.item_layout.view.*

class OriginalAdapter(
    private val itemEditClicked: (Item) -> Unit,
    private val itemDeleteClicked: (Item) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = mutableListOf<Item>()
    var user: User? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val listItemView = parent.inflate(R.layout.item_layout)
        return ViewHolder(listItemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Item) = with(itemView) {
            titleTV.text = item.fileMetaData.name
            categoryTV.text = item.fileMetaData.ext
            descriptionTV.text = item.fileMetaData.path
            sizeTV.text = item.fileMetaData.size
            dateTV.text = getTimeAgo(item.time)

            if (user?.imageUrl?.isNotEmpty() == true) {
                Glide.with(context).load(user?.imageUrl).placeholder(R.drawable.ic_icon_user)
                    .dontAnimate()
                    .into(userImage)
                userImage.shouldShow = true
                userImageText.shouldShow = false
            } else if (user?.name?.isNotEmpty() == true) {
                userImageText.text = user?.name?.substring(0, 1)
                userImage.shouldShow = false
                userImageText.shouldShow = true
            }

            val uri = Uri.parse(item.fileMetaData.path)
            val mediaController = MediaController(context)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(uri)
            videoView.seekTo(1)

            moreOptions.setOnClickListener {
                showPopup(item, context, it)
            }
        }
    }

    private fun showPopup(item: Item, context: Context, view: View) {
        val popup: PopupMenu?
        popup = PopupMenu(context, view)
        popup.inflate(R.menu.menu_more_options)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            when (it?.itemId) {
                R.id.itemEdit -> {
                    itemEditClicked(item)
                }
                R.id.itemDelete -> {
                    itemDeleteClicked(item)
                }
            }
            true
        })

        popup.show()
    }
}