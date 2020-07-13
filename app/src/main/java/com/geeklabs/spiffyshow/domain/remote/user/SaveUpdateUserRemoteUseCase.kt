package com.geeklabs.spiffyshow.domain.remote.user

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.UseCase
import io.reactivex.Single
import javax.inject.Inject

class SaveUpdateUserRemoteUseCase @Inject constructor(private val dataRepository: DataRepository) :
    UseCase<User, Single<User>>() {
    override fun execute(parameters: User) = dataRepository.saveUpdateUser(parameters)
}