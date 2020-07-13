package com.geeklabs.spiffyshow.domain.local.user

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Single
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<String, Single<User>>() {
    override fun execute(parameters: String) = dataRepository.login(parameters)
}