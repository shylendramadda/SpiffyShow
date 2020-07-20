package com.geeklabs.spiffyshow.domain.remote.like

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Like
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Single
import okhttp3.ResponseBody
import javax.inject.Inject

class SaveLikeRemoteUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Like, Single<ResponseBody>>() {
    override fun execute(parameters: Like) = dataRepository.saveLikeRemote(parameters)
}