package com.geeklabs.spiffyshow.domain.remote.original

import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.domain.UseCase
import com.geeklabs.spiffyshow.utils.FileUtil
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val dataRepository: DataRepository,
    private val fileUtil: FileUtil
) :
    UseCase<Original, Single<ResponseBody>>() {
    override fun execute(parameters: Original): Single<ResponseBody> {
        val filePath = parameters.fileMetaData?.path ?: throw Exception("File path is empty")
        val file = File(filePath)
        val mimeType = fileUtil.getMimeType(parameters.fileMetaData.ext) ?: DEFAULT_MIME_TYPE
        val requestBody = RequestBody.create(MediaType.parse(mimeType), file)
        val body = MultipartBody.Part.createFormData(FILE_PATH, file.name, requestBody)
        return dataRepository.uploadFile(body, file.name)
    }

    companion object {
        const val DEFAULT_MIME_TYPE = "multipart/form-data"
        const val FILE_PATH = "filePath"
    }
}