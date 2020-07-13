package com.geeklabs.spiffyshow.data.remote.services.providers

interface ApiServiceProvider<T>{
    fun getService(): T?
    fun setUrl(url: String)
}

