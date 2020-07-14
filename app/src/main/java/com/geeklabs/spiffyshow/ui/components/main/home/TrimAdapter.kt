package com.geeklabs.spiffyshow.ui.components.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.extensions.inflate
import com.geeklabs.spiffyshow.extensions.setTint
import com.geeklabs.spiffyshow.extensions.shouldShow
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.utils.Utils.getTimeAgo
import com.jarvanmo.exoplayerview.media.SimpleMediaSource
import kotlinx.android.synthetic.main.item_layout.view.*

class TrimAdapter(
    private val itemEditClicked: (Trim) -> Unit,
    private val itemDeleteClicked: (Trim) -> Unit,
    private val onItemShareClicked: (Trim) -> Unit,
    private val onProfileClicked: (User) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = mutableListOf<Trim>()
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
        fun bind(item: Trim) = with(itemView) {
            shareIV.visible = true
            titleTV.text = item.title
            categoryTV.text = item.category
            descriptionTV.text = item.description
            viewsTV.text = "${adapterPosition + 2} Views"
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
            val simpleMediaSource = SimpleMediaSource(uri)
            videoView.play(simpleMediaSource, false)

            moreOptions.setOnClickListener {
                showPopup(item, context, it)
            }
            shareIV.setOnClickListener {
                onItemShareClicked(item)
            }
            userImageLayout.setOnClickListener {
                onProfileClicked(user!!)
            }
            var likeCount = 10
            likeIV.setOnClickListener {
                ++likeCount
                likesTV.text = "$likeCount Likes"
                likeIV.setTint(R.color.blueTertiary)
            }
        }
    }

    private fun showPopup(item: Trim, context: Context, view: View) {
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