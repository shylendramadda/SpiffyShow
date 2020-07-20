package com.geeklabs.spiffyshow.domain.local.original

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.domain.UseCase
import javax.inject.Inject

class SaveUpdateOriginalsInLocalUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<MutableList<Original>, Unit>() {
    override fun execute(parameters: MutableList<Original>) =
        dataRepository.saveOriginalsInLocal(parameters)
}
