package com.geeklabs.spiffyshow.domain.remote.trim

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Single
import javax.inject.Inject

class FetchTrimsRemoteUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Unit, Single<MutableList<Trim>>>() {
    override fun execute(parameters: Unit) = dataRepository.getTrimsFromRemote()
}
