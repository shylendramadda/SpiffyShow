package com.geeklabs.spiffyshow.domain.local.file

import com.geeklabs.spiffyshow.domain.UseCase
import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.utils.FileUtil
import io.reactivex.Single
import javax.inject.Inject

class ProcessOnlineFileUseCase @Inject constructor(private val fileUtil: FileUtil) :
    UseCase<FileMetaData, Single<FileMetaData>>() {
    override fun execute(parameters: FileMetaData) = Single.create<FileMetaData> { emitter ->
        try {
            val filePath = fileUtil.writeOnlineFileToCache(parameters)
            parameters.isOnlineFile = false
            parameters.path = filePath ?: ""
            emitter.onSuccess(parameters)
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }
}