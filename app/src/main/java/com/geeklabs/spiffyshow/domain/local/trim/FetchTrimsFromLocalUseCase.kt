package com.geeklabs.spiffyshow.domain.local.trim

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

class FetchTrimsFromLocalUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Unit, Flowable<MutableList<Trim>>>() {
    override fun execute(parameters: Unit) = dataRepository.getTrimsFromLocal()
}
