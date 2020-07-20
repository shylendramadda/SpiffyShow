package com.geeklabs.spiffyshow.data.local.models.item

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.geeklabs.spiffyshow.models.FileMetaData

@Entity
data class Original(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long = 0,
    val catId: Long = 0,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val time: Long = 0,
    val likes: Long = 0,
    val comments: MutableList<Comment> = mutableListOf(),
    val fileMetaData: FileMetaData? = null
)