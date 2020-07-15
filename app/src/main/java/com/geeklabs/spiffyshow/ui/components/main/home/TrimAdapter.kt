package com.geeklabs.spiffyshow.ui.components.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.media.MediaPlayer
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
import com.geeklabs.spiffyshow.extensions.shouldShow
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.utils.Utils.getTimeAgo
import com.universalvideoview.UniversalMediaController
import com.universalvideoview.UniversalVideoView
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
    private var videoViewUniversal: UniversalVideoView? = null
    private var videoControllerUniversal: UniversalMediaController? = null

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
            shareTV.visible = true
            titleTV.text = item.title
            categoryTV.text = item.category
            descriptionTV.text = item.description
            viewsTV.text = "${adapterPosition + 2} Views"
            dateTV.text = getTimeAgo(item.time)
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
                showPopup(item, context, it)
            }
            commentTV.setOnClickListener {
                onCommentClicked(item)
            }
            shareTV.setOnClickListener {
                onItemShareClicked(item)
            }
            userImageLayout.setOnClickListener {
                onProfileClicked(user!!)
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

            val uri = Uri.parse(item.fileMetaData.path)
            videoViewUniversal = videoViewUni
            videoControllerUniversal = mediaController
            videoViewUniversal?.setMediaController(videoControllerUniversal)
            videoViewUniversal?.setVideoURI(uri)
            videoViewUniversal?.seekTo(1)
            videoViewUniversal?.setVideoViewCallback(object : UniversalVideoView.VideoViewCallback {
                override fun onBufferingStart(mediaPlayer: MediaPlayer?) {
                }

                override fun onBufferingEnd(mediaPlayer: MediaPlayer?) {
                }

                override fun onPause(mediaPlayer: MediaPlayer?) {
                    mediaPlayer?.pause()
                }

                override fun onScaleChange(isFullscreen: Boolean) {
                }

                override fun onStart(mediaPlayer: MediaPlayer?) {
                    mediaPlayer?.start()
                }
            })
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