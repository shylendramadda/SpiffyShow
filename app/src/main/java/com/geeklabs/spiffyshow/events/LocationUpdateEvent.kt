package com.geeklabs.spiffyshow.events

import android.location.Location

data class LocationUpdateEvent(var location: Location? = null)