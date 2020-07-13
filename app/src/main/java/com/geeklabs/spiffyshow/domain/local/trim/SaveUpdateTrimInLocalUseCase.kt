package com.geeklabs.spiffyshow.domain.local.trim

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.domain.UseCase
import javax.inject.Inject

class SaveUpdateTrimInLocalUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<MutableList<Trim>, Unit>() {
    override fun execute(parameters: MutableList<Trim>) = dataRepository.saveTrimInLocal(parameters)
}
