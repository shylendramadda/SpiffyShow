package com.geeklabs.spiffyshow.data.local.models.user

data class Address(
    var pinCode: String? = null,
    var city: String? = null,
    var state: String? = null,
    var country: String? = null,
    var addressInfo: String? = null,
    var landMark: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
)