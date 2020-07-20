package com.geeklabs.spiffyshow.domain.remote.comment

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Comment
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Single
import okhttp3.ResponseBody
import javax.inject.Inject

class SaveCommentUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Comment, Single<ResponseBody>>() {
    override fun execute(parameters: Comment) = dataRepository.saveCommentRemote(parameters)
}