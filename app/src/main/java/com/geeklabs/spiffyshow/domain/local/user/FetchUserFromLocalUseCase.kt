package com.geeklabs.spiffyshow.domain.local.user

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.domain.UseCase
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.PrefManager
import io.reactivex.Single
import javax.inject.Inject

class FetchUserFromLocalUseCase @Inject constructor(
    private val dataRepository: DataRepository,
    private val prefManager: PrefManager
) :
    UseCase<Unit, Single<User>>() {
    override fun execute(parameters: Unit): Single<User> {
        val userId = prefManager.getString(Constants.USER_ID, "0")?.toLong() ?: 0
        return dataRepository.getUserById(userId)
    }
}