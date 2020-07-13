package com.geeklabs.spiffyshow.domain.local.item

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.domain.UseCase
import javax.inject.Inject

class SaveUpdateItemsInLocalUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<MutableList<Item>, Unit>() {
    override fun execute(parameters: MutableList<Item>) =
        dataRepository.saveItemsInLocal(parameters)
}
