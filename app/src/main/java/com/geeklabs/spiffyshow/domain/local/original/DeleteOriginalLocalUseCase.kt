package com.geeklabs.spiffyshow.domain.local.original

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.domain.UseCase
import javax.inject.Inject

class DeleteOriginalLocalUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Long, Unit>() {
    override fun execute(parameters: Long) = dataRepository.deleteOriginalLocal(parameters)
}