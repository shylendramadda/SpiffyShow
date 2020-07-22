package com.geeklabs.spiffyshow.ui.components.main.search

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.extensions.inflate
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter(
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val listItemView = parent.inflate(R.layout.item_search)
        return ViewHolder(listItemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) = with(itemView) {
            searchTextTV.text = "$item related videos"
            setOnClickListener {
                onItemClicked(item)
            }
        }
    }
}