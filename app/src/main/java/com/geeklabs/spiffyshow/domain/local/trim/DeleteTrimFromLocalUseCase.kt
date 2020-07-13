package com.geeklabs.spiffyshow.domain.local.trim

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.domain.UseCase
import javax.inject.Inject

class DeleteTrimFromLocalUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Long, Unit>() {
    override fun execute(parameters: Long) = dataRepository.deleteTrimFromLocal(parameters)
}