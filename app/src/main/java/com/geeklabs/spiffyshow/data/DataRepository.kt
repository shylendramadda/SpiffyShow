package com.geeklabs.spiffyshow.data

import com.geeklabs.spiffyshow.data.local.LocalDataSource
import com.geeklabs.spiffyshow.data.local.models.item.Comment
import com.geeklabs.spiffyshow.data.local.models.item.Like
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.data.local.models.location.DeviceLocation
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.data.remote.RemoteDataSource
import okhttp3.MultipartBody
import javax.inject.Inject

open class DataRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    // Local calls
    fun saveOriginalsInLocal(originals: MutableList<Original>) =
        localDataSource.saveOriginals(originals)

    fun getOriginalsFromLocal() = localDataSource.getOriginals()
    fun getOriginalsByCategoryId(categoryId: Long) =
        localDataSource.getOriginalsByCategoryId(categoryId)

    fun deleteOriginalLocal(id: Long) = localDataSource.deleteOriginal(id)
    fun saveTrimInLocal(items: MutableList<Trim>) = localDataSource.saveTrims(items)
    fun getTrimsFromLocal() = localDataSource.getTrims()
    fun deleteTrimFromLocal(id: Long) = localDataSource.deleteTrim(id)
    fun saveUser(user: User) = localDataSource.save(user)
    fun getUsersCount() = localDataSource.getUserCount()
    fun getUserById(id: Long) = localDataSource.getUserById(id)
    fun getDeviceLocation() = localDataSource.getDeviceLocation()
    fun saveDeviceLocation(deviceLocation: DeviceLocation) =
        localDataSource.saveDeviceLocation(deviceLocation)

    // Remote calls
    fun login(phoneNumber: String) = remoteDataSource.login(phoneNumber)
    fun saveUpdateUser(user: User) = remoteDataSource.saveUpdateUser(user)
    fun getItemsFromRemote() = remoteDataSource.getItemsFromRemote()
    fun addTokenInHeaderInterceptor(registrationToken: String) =
        remoteDataSource.addTokenInHeaderInterceptor(registrationToken)

    fun addBasicAuthHeaderInterceptor(userName: String, password: String) =
        remoteDataSource.addBasicAuthHeaderInterceptor(userName, password)

    fun saveOriginalRemote(original: Original) = remoteDataSource.saveOriginal(original)
    fun uploadFile(file: MultipartBody.Part, name: String) = remoteDataSource.uploadFile(file, name)
    fun saveTrimRemote(trim: Trim) = remoteDataSource.saveTrim(trim)
    fun saveLikeRemote(like: Like) = remoteDataSource.saveLike(like)
    fun saveCommentRemote(comment: Comment) = remoteDataSource.saveComment(comment)
    fun getTrimsFromRemote() = remoteDataSource.getTrims()
    fun followUser(toUserId: Long) = remoteDataSource.followUser(toUserId)
}