package com.geeklabs.spiffyshow.domain.local.item

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

class FetchItemsByCategoryUseCase @Inject constructor(
    private val dataRepository: DataRepository
) : UseCase<Long, Flowable<MutableList<Item>>>() {
    override fun execute(parameters: Long) =
        dataRepository.getItemsByCategoryId(parameters)
}