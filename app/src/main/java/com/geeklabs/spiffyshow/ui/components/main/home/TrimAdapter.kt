package com.geeklabs.spiffyshow.ui.components.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.extensions.inflate
import com.geeklabs.spiffyshow.extensions.shouldShow
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.utils.Utils.getTimeAgo
import kotlinx.android.synthetic.main.item_layout.view.*

class TrimAdapter(
    private val onEditClicked: (Trim) -> Unit,
    private val onDeleteClicked: (Trim) -> Unit,
    private val onCommentClicked: (Trim) -> Unit,
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
        fun bind(trim: Trim) = with(itemView) {
            shareTV.visible = true
            titleTV.text = trim.title
            categoryTV.text = trim.originalUrl
            categoryTV.setTextColor(context.getColor(R.color.blueTertiary))
            descriptionTV.text = trim.description
            viewsTV.text = "${adapterPosition + 2} Views"
            dateTV.text = getTimeAgo(trim.time)
            likeTV.text = "20"
            commentTV.text = "5"

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

            moreOptions.setOnClickListener {
                showPopup(trim, context, it)
            }
            commentTV.setOnClickListener {
                onCommentClicked(trim)
            }
            shareTV.setOnClickListener {
                onItemShareClicked(trim)
            }
            userImageLayout.setOnClickListener {
                onProfileClicked(user!!)
            }
            followText.setOnClickListener {
                followText.text = context.getString(R.string.following)
            }
            var likeCount = 20
            var isLikeClicked = false
            likeTV.setOnClickListener {
                isLikeClicked = !isLikeClicked
                if (isLikeClicked) {
                    ++likeCount
                    likeTV.setTextColor(context.getColor(R.color.blueTertiary))
                    likeTV.compoundDrawableTintList =
                        ColorStateList.valueOf(context.getColor(R.color.blueTertiary))
                } else {
                    --likeCount
                    likeTV.setTextColor(context.getColor(R.color.grayPrimary))
                    likeTV.compoundDrawableTintList =
                        ColorStateList.valueOf(context.getColor(R.color.grayPrimary))
                }
                likeTV.text = "$likeCount"
            }

            if (trim.fileMetaData!!.path.isNotEmpty() && trim.fileMetaData.size.isEmpty()) { // if youtube URL
                youtubePlayer.visible = true
                universalVideoView.visible = false
                youtubePlayer.setYoutubeView(trim.fileMetaData.path)
            } else {
                youtubePlayer.visible = false
                universalVideoView.visible = true
                universalVideoView.setVideoView(trim.fileMetaData.path, trim.title)
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
                    onEditClicked(item)
                }
                R.id.itemDelete -> {
                    onDeleteClicked(item)
                }
            }
            true
        })

        popup.show()
    }
}