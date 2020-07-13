package com.geeklabs.spiffyshow.data.local.models.notification

data class Notification(
    val id: Long = 0,
    var title: String = "",
    var desc: String = "",
    var time: Long = 0,
    var imageUrl: String = ""
)