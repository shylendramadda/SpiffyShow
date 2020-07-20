package com.geeklabs.spiffyshow.ui.components.comment

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Comment
import com.geeklabs.spiffyshow.extensions.inflate
import com.geeklabs.spiffyshow.extensions.shouldShow
import com.geeklabs.spiffyshow.utils.Utils
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    var list = mutableListOf<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = parent.inflate(R.layout.item_comment)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(comment: Comment) = with(itemView) {
            nameTV.text = comment.userName
            commentTV.text = comment.text
            dateTV.text = Utils.getTimeAgo(comment.time)

            if (comment.imageUrl?.isNotEmpty() == true) {
                Glide.with(context).load(comment.imageUrl).placeholder(R.drawable.ic_icon_user)
                    .dontAnimate()
                    .into(userImage)
                userImage.shouldShow = true
                userImageText.shouldShow = false
            } else if (comment.userName?.isNotEmpty() == true) {
                userImageText.text = comment.userName.substring(0, 1)
                userImage.shouldShow = false
                userImageText.shouldShow = true
            }
        }
    }
}