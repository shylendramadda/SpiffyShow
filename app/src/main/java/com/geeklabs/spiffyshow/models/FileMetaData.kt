package com.geeklabs.spiffyshow.models

data class FileMetaData(
    var name: String = "",
    var size: String = "",
    var path: String = "",
    val ext: String = "",
    var uri: String = "",
    var length: Long = 0,
    var isOnlineFile: Boolean = false
)