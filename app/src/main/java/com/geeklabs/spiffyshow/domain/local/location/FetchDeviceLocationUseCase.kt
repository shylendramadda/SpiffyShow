package com.geeklabs.spiffyshow.domain.local.location

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.domain.UseCase
import com.geeklabs.spiffyshow.data.local.models.location.DeviceLocation
import io.reactivex.Single
import javax.inject.Inject

class FetchDeviceLocationUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<Unit, Single<DeviceLocation>>() {
    override fun execute(parameters: Unit) = dataRepository.getDeviceLocation()
}