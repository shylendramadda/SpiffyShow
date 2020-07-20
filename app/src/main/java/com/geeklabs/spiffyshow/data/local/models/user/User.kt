package com.geeklabs.spiffyshow.data.local.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    var id: Long = 0,
    var role: String? = null,
    var name: String? = null,
    var phoneNumber: String? = null,
    var email: String? = null,
    var imageUrl: String? = null,
    var interests: String? = null,
    var bio: String? = null,
    var followers: Long = 0,
    var following: Long = 0,
    var trims: Long = 0,
    var originals: Long = 0,
    val firebaseToken: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var pinCode: String? = null,
    var city: String? = null,
    var state: String? = null,
    var country: String? = null,
    var addressInfo: String? = null
)