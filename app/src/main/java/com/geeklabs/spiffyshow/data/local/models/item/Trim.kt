package com.geeklabs.spiffyshow.data.local.models.item

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.geeklabs.spiffyshow.models.FileMetaData

@Entity
data class Trim(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long = 0,
    val catId: Long = 0,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val time: Long = 0,
    val fileMetaData: FileMetaData? = null
)