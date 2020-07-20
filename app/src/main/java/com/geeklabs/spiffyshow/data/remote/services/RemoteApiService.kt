package com.geeklabs.spiffyshow.data.remote.services

import com.geeklabs.spiffyshow.data.local.models.item.Comment
import com.geeklabs.spiffyshow.data.local.models.item.Like
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*


interface RemoteApiService {

    // User
    @POST("user/login")
    fun login(@Body phoneNumber: String): Single<User>

    @POST("user")
    fun saveUpdateUser(@Body user: User): Single<User>

    @GET("item")
    fun getItems(): Single<MutableList<Original>>

    @POST("original")
    fun saveOriginal(original: Original): Single<Original>

    @Multipart
    @POST("item/upload")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Query("fileName") name: String
    ): Single<ResponseBody>

    @POST("trim")
    fun saveTrim(trim: Trim): Single<Trim>

    @POST("like")
    fun saveLike(@Body like: Like): Single<ResponseBody>

    @POST("comment")
    fun saveComment(@Body comment: Comment): Single<ResponseBody>

    @GET("trim")
    fun getTrims(): Single<MutableList<Trim>>

    @POST("follow/{id}")
    fun followUser(@Path("id") toUserId: Long): Single<ResponseBody>
}