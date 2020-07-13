package com.geeklabs.spiffyshow.data.remote

import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.data.remote.interceptors.HeaderInterceptor
import com.geeklabs.spiffyshow.data.remote.services.RemoteApiService
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
}