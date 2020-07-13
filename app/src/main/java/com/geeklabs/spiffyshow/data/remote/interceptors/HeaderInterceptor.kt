package com.geeklabs.spiffyshow.data.remote.interceptors

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class HeaderInterceptor @Inject constructor() : Interceptor {
    private var registrationToken: String? = null
    private var credentials: String? = null

    fun setRegistrationToken(registrationToken: String) {
        this.registrationToken = registrationToken
    }

    fun setBasicAuthentication(userName: String, password: String) {
        this.credentials = Credentials.basic(userName, password)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        requestBuilder.addHeader("Accept", "application/json")
        requestBuilder.addHeader("Content-Type", "application/json")
        /*if (registrationToken != null) {
            requestBuilder.addHeader("registration-token", registrationToken!!)
        }*/
        if (credentials != null) {
            requestBuilder.addHeader("Authorization", credentials!!)
        }
        return chain.proceed(requestBuilder.build())
    }
}