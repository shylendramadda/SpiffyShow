package com.geeklabs.spiffyshow.data.remote.models

data class ApiResponse<T>(
    val code: Int = 0,
    val message: String = "",
    val data: T
)