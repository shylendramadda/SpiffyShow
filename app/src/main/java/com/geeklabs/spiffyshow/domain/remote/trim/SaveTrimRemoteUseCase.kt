package com.geeklabs.spiffyshow.domain.remote.trim

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Single
import javax.inject.Inject

class SaveTrimRemoteUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Trim, Single<Trim>>() {
    override fun execute(parameters: Trim) = dataRepository.saveTrimRemote(parameters)
}