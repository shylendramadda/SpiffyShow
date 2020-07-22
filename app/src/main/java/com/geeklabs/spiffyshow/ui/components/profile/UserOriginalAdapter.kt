package com.geeklabs.spiffyshow.ui.components.profile

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.extensions.inflate
import com.geeklabs.spiffyshow.extensions.visible
import kotlinx.android.synthetic.main.item_clip.view.youtubePlayer
import kotlinx.android.synthetic.main.item_layout.view.*

class UserOriginalAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var originals = mutableListOf<Original>()
    var user: User? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val listItemView = parent.inflate(R.layout.item_clip)
        return ViewHolder(listItemView)
    }

    override fun getItemCount() = originals.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(originals[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(original: Original) = with(itemView) {
            if (original.fileMetaData!!.path.isNotEmpty() && original.fileMetaData.size.isEmpty()) {
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

}