package com.geeklabs.spiffyshow.domain.remote.original

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Single
import javax.inject.Inject

class SaveOriginalRemoteUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Original, Single<Original>>() {
    override fun execute(parameters: Original) = dataRepository.saveOriginalRemote(parameters)
}
