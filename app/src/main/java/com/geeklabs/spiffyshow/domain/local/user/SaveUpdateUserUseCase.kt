package com.geeklabs.spiffyshow.domain.local.user

import androidx.annotation.WorkerThread
import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.UseCase
import javax.inject.Inject

@WorkerThread
class SaveUpdateUserUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<User, Unit>() {
    override fun execute(parameters: User) {
        val userCount = dataRepository.getUsersCount().blockingGet()
        if (userCount > 0) {
            val user = dataRepository.getUserById(parameters.id).blockingGet()
            if (user != null) {
                user.name = parameters.name ?: user.name
                user.role = parameters.role ?: user.role
                user.phoneNumber = parameters.phoneNumber ?: user.phoneNumber
                user.email = parameters.email ?: user.email
                user.imageUrl = parameters.imageUrl ?: user.imageUrl
                user.pinCode = parameters.pinCode ?: user.pinCode
                user.city = parameters.city ?: user.city
                user.state = parameters.state ?: user.state
                user.country = parameters.country ?: user.country
                user.addressInfo = parameters.addressInfo ?: user.addressInfo
                user.landMark = parameters.landMark ?: user.landMark
                user.latitude = parameters.latitude ?: user.latitude
                user.longitude = parameters.longitude ?: user.longitude
                dataRepository.saveUser(user)
            }
        } else {
            dataRepository.saveUser(parameters)
        }
    }
}