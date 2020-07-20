package com.geeklabs.spiffyshow.data.remote

import com.geeklabs.spiffyshow.data.local.models.item.Comment
import com.geeklabs.spiffyshow.data.local.models.item.Like
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.data.remote.interceptors.HeaderInterceptor
import com.geeklabs.spiffyshow.data.remote.services.RemoteApiService
import okhttp3.MultipartBody
import javax.inject.Inject

open class RemoteDataSource @Inject constructor(
    private val headerInterceptor: HeaderInterceptor,
    private val remoteApiService: RemoteApiService
) {
    fun addTokenInHeaderInterceptor(registrationToken: String) {
        headerInterceptor.setRegistrationToken(registrationToken)
    }

    fun addBasicAuthHeaderInterceptor(userName: String, password: String) {
        headerInterceptor.setBasicAuthentication(userName, password)
    }

    fun login(phoneNumber: String) = remoteApiService.login(phoneNumber)
    fun saveUpdateUser(user: User) = remoteApiService.saveUpdateUser(user)
    fun getItemsFromRemote() = remoteApiService.getItems()
    fun saveOriginal(original: Original) = remoteApiService.saveOriginal(original)
    fun uploadFile(file: MultipartBody.Part, name: String) = remoteApiService.uploadFile(file, name)
    fun saveTrim(trim: Trim) = remoteApiService.saveTrim(trim)
    fun saveLike(like: Like) = remoteApiService.saveLike(like)
    fun saveComment(comment: Comment) = remoteApiService.saveComment(comment)
    fun getTrims() = remoteApiService.getTrims()
    fun followUser(toUserId: Long) = remoteApiService.followUser(toUserId)
}