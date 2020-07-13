package com.geeklabs.spiffyshow.models

data class EmailRequest(
    val from: String,
    val fromPassword: String,
    val to: List<String> = listOf(),
    val subject: String = "Subject",
    val body: String = "Body"
)