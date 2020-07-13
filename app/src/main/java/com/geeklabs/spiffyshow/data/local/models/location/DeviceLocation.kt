package com.geeklabs.spiffyshow.data.local.models.location

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeviceLocation(
    @PrimaryKey
    val id: Long = 0,
    var city: String = "",
    var state: String = "",
    var country: String = "",
    var pinCode: String = "",
    var addressLine: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)