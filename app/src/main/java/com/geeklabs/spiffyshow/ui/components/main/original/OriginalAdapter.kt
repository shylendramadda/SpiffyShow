package com.geeklabs.spiffyshow.ui.components.main.original

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.extensions.inflate
import com.geeklabs.spiffyshow.extensions.shouldShow
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.utils.Utils
import com.geeklabs.spiffyshow.utils.Utils.getTimeAgo
import kotlinx.android.synthetic.main.item_layout.view.*

class OriginalAdapter(
    private val onEditClicked: (Original, Boolean) -> Unit,
    private val onDeleteClicked: (Original) -> Unit,
    private val onCommentClicked: (Original) -> Unit,
    private val onProfileClicked: (User) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = mutableListOf<Original>()
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
        fun bind(original: Original) = with(itemView) {
            trimTV.visible = true
            Utils.showHideViews(false, shareTV, viewsTV)
            titleTV.text = original.title
            categoryTV.text = original.category
            descriptionTV.text = original.description
            dateTV.text = getTimeAgo(original.time)
            likeTV.text = "15"
            commentTV.text = "5"

            if (original.fileMetaData!!.path.isNotEmpty() && original.fileMetaData.size.isEmpty()) {
                trimTV.visible = false
            }

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
                showPopup(original, context, it)
            }
            commentTV.setOnClickListener {
                onCommentClicked(original)
            }
            trimTV.setOnClickListener {
                onEditClicked(original, true)
            }
            userImageLayout.setOnClickListener {
                onProfileClicked(user!!)
            }
            followText.setOnClickListener {
                followText.text = context.getString(R.string.following)
            }
            var likeCount = 15
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

            if (original.fileMetaData.path.isNotEmpty() && original.fileMetaData.size.isEmpty()) {
                youtubePlayer.visible = true
                universalVideoView.visible = false
                youtubePlayer.setYoutubeView(original.fileMetaData.path)
            } else {
                youtubePlayer.visible = false
                universalVideoView.visible = true
                universalVideoView.setVideoView(original.fileMetaData.path)
            }
        }
    }

    private fun showPopup(original: Original, context: Context, view: View) {
        val popup: PopupMenu?
        popup = PopupMenu(context, view)
        popup.inflate(R.menu.menu_more_options)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
            when (it?.itemId) {
                R.id.itemEdit -> {
                    onEditClicked(original, false)
                }
                R.id.itemDelete -> {
                    onDeleteClicked(original)
                }
            }
            true
        })
        popup.show()
    }
}