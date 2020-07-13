package com.geeklabs.spiffyshow.domain.remote.item

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Item
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Single
import javax.inject.Inject

class FetchItemsRemoteUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Unit, Single<MutableList<Item>>>() {
    override fun execute(parameters: Unit) = dataRepository.getItemsFromRemote()
}
