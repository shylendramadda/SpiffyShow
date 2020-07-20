package com.geeklabs.spiffyshow.domain.remote.user

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Single
import okhttp3.ResponseBody
import javax.inject.Inject

class FollowUserUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Long, Single<ResponseBody>>() {
    override fun execute(parameters: Long) = dataRepository.followUser(parameters)
}