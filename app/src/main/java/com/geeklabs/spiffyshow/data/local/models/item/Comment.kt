package com.geeklabs.spiffyshow.data.local.models.item

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val byName: String? = null,
    val imageUrl: String? = null,
    val text: String? = null,
    val time: Long = 0
)