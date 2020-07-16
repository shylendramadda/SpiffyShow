package com.geeklabs.spiffyshow.domain.local.file

import com.geeklabs.spiffyshow.domain.UseCase
import com.geeklabs.spiffyshow.enums.StringEnum
import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.FileUtil
import com.geeklabs.spiffyshow.utils.StringUtils
import io.reactivex.Single
import java.io.File
import javax.inject.Inject

class ProcessFileUriUseCase @Inject constructor(
    private val fileUtil: FileUtil,
    private val stringUtils: StringUtils,
    private val processOnlineFileUseCase: ProcessOnlineFileUseCase
) : UseCase<String, Single<Pair<FileMetaData, String>>>() {
    override fun execute(parameters: String): Single<Pair<FileMetaData, String>> {
        var errorMessage = ""
        var fileMetadata: FileMetaData? = null
        try {
            fileMetadata = fileUtil.getFileMetaData(parameters)
            when {
                fileMetadata == null || (!fileMetadata.isOnlineFile && fileMetadata.path.isEmpty()) -> {
                    errorMessage = stringUtils.getString(StringEnum.FILE_PATH_ERROR.resId)
                }
                !fileMetadata.isOnlineFile && !File(fileMetadata.path).exists() -> {
                    errorMessage = stringUtils.getString(StringEnum.FILE_NOT_EXISTS_ERROR.resId)
                }
                fileUtil.getFileSizeInMb(fileMetadata.length)
                    .toDouble() > Constants.MAX_FILE_SIZE -> {
                    errorMessage = stringUtils.getString(StringEnum.FILE_SIZE_ERROR.resId)
                }
                fileMetadata.isOnlineFile -> {
                    fileMetadata = processOnlineFileUseCase.execute(fileMetadata).blockingGet()
                }
            }
        } catch (e: Exception) {
            errorMessage = stringUtils.getString(StringEnum.FILE_PATH_ERROR.resId)
        }
        return Single.just(Pair(fileMetadata!!, errorMessage))
    }
}