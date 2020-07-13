package com.geeklabs.spiffyshow.domain.local.item

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.domain.UseCase
import javax.inject.Inject

class DeleteItemLocalUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Long, Unit>() {
    override fun execute(parameters: Long) = dataRepository.deleteItemLocal(parameters)
}