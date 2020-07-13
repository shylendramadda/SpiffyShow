package com.geeklabs.spiffyshow.domain.local.location

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.domain.UseCase
import com.geeklabs.spiffyshow.data.local.models.location.DeviceLocation
import javax.inject.Inject

class SaveDeviceLocationUseCase @Inject constructor(
    private val
    dataRepository: DataRepository
) : UseCase<DeviceLocation, Unit>() {
    override fun execute(parameters: DeviceLocation) = dataRepository.saveDeviceLocation(parameters)
}