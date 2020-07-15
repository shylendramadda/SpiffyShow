package com.geeklabs.spiffyshow.ui.components.profile

import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.extensions.inflate
import com.universalvideoview.UniversalMediaController
import com.universalvideoview.UniversalVideoView
import kotlinx.android.synthetic.main.item_clip.view.*

class UserOriginalAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = mutableListOf<Item>()
    var user: User? = null
    private var videoViewUniversal: UniversalVideoView? = null
    private var videoControllerUniversal: UniversalMediaController? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val listItemView = parent.inflate(R.layout.item_clip)
        return ViewHolder(listItemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item) = with(itemView) {
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

}