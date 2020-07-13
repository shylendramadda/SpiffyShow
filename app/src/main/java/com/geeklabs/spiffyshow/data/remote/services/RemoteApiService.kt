package com.geeklabs.spiffyshow.data.remote.services

import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.data.local.models.user.User
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface RemoteApiService {

    // User
    @POST("user/login")
    fun login(@Body phoneNumber: String): Single<User>

    @POST("user")
    fun saveUpdateUser(@Body user: User): Single<User>

    @GET("item")
    fun getItems(): Single<MutableList<Item>>
}