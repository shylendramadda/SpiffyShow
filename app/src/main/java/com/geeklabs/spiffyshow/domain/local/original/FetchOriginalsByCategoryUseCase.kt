package com.geeklabs.spiffyshow.domain.local.original

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

class FetchOriginalsByCategoryUseCase @Inject constructor(
    private val dataRepository: DataRepository
) : UseCase<Long, Flowable<MutableList<Original>>>() {
    override fun execute(parameters: Long) =
        dataRepository.getOriginalsByCategoryId(parameters)
}