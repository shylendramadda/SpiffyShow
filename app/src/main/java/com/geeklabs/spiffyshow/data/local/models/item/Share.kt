package com.geeklabs.spiffyshow.data.local.models.item

data class Share(
    val id: Long = 0,
    val userId: String? = null,
    val userName: String? = null,
    val imageUrl: String? = null,
    val time: Long = 0,
    val trimId: Long = 0,
    val originalId: Long = 0
)