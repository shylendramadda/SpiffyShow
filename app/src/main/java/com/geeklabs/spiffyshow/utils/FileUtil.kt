package com.geeklabs.spiffyshow.utils

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.DecimalFormat
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.geeklabs.spiffyshow.extensions.saveBitmap
import com.geeklabs.spiffyshow.models.FileMetaData
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject


class FileUtil @Inject constructor(val context: Context) {

    fun isValidImage(filePath: String): Boolean {
        try {
            BitmapFactory.decodeFile(filePath).getPixel(0, 0)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun getFileName(filePath: String): String {
        val fileNameWithExtension = File(filePath).name
        return fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf("."))
    }

    fun getFileExtension(filePath: String?): String { // returns png jpg etc..
        return filePath?.substring(filePath.lastIndexOf(".") + 1)?.toLowerCase(Locale.getDefault())
            ?: ""
    }

    fun writeByteArrayToFile(
        bytes: ByteArray, fileName: String,
        extension: String, filePath: String
    ): String {
        val cacheFile = File(filePath)
        if (!cacheFile.exists()) {
            cacheFile.mkdir()
        }
        val file = File(cacheFile, "$fileName.$extension")
        file.writeBytes(bytes)
        return file.path
    }

    fun writeBitmapToFile(bitmap: Bitmap, fileName: String, filePath: String): File {
        val cacheFile = File(filePath)
        if (!cacheFile.exists()) {
            cacheFile.mkdir()
        }
        val imageFile = File(cacheFile, fileName)
        imageFile.saveBitmap(bitmap)
        return imageFile
    }

    fun getFileSizeInMb(sizeInBytes: Long): String {
        val decimalFormat = DecimalFormat("0.00")
        if (sizeInBytes <= 0) return "0"
        return decimalFormat.format(sizeInBytes / (1024.0f * 1024.0f))
    }

    private fun getFileSize(size: Long): String {
        val decimalFormat = DecimalFormat("0.00")
        val sizeKb = 1024.0f
        val sizeMb = sizeKb * sizeKb
        val sizeGb = sizeMb * sizeKb
        val sizeTerra = sizeGb * sizeKb
        return when {
            size < sizeMb -> decimalFormat.format(size / sizeKb) + " KB"
            size < sizeGb -> decimalFormat.format(size / sizeMb) + " MB"
            size < sizeTerra -> decimalFormat.format(size / sizeGb) + " GB"
            else -> ""
        }
    }

    fun getFileMetaData(fileUri: String): FileMetaData? {
        val uri = Uri.parse(fileUri)
        when {
            // DocumentProvider
            DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    // ExternalStorageProvider
                    isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        if (split.isEmpty()) return null

                        val type = split[0]
                        // This is for checking Main Memory
                        if ("primary".equals(type, ignoreCase = true)) {
                            if (split.size > 1) {
                                val fileName = split[1]
                                val filePath = Environment.getExternalStorageDirectory()
                                    .toString() + "/" + split[1]
                                val length = File(filePath).length()
                                return FileMetaData(
                                    fileName,
                                    getFileSize(length),
                                    filePath,
                                    getFileExtension(filePath),
                                    uri.toString(),
                                    length
                                )
                            }
                        } else {
                            val fileName = docId.replace(":", "/")
                            val filePath = "storage" + "/" + docId.replace(":", "/")
                            val length = File(filePath).length()
                            return FileMetaData(
                                fileName,
                                getFileSize(length),
                                filePath,
                                getFileExtension(filePath),
                                uri.toString(),
                                length
                            )
                        }
                    }
                    isDownloadsDocument(uri) -> {
                        val fileMetaData = createFileMetaData(
                            uri = uri,
                            columnName = MediaStore.MediaColumns.DISPLAY_NAME
                        )
                        if (fileMetaData?.name != null) {
                            val filePath = Environment.getExternalStorageDirectory()
                                .toString() + "/Download/" + fileMetaData.name + "." + fileMetaData.ext
                            fileMetaData.path = filePath
                            return fileMetaData
                        }
                        var fileId = DocumentsContract.getDocumentId(uri)
                        if (fileId.startsWith("raw:")) {
                            fileId = fileId.replaceFirst("raw:".toRegex(), "")
                            val file = File(fileId)
                            if (file.exists()) {
                                val fileName = file.name
                                val filePath = fileId
                                val length = File(filePath).length()
                                return FileMetaData(
                                    fileName,
                                    getFileSize(length),
                                    filePath,
                                    getFileExtension(filePath),
                                    uri.toString(),
                                    length
                                )
                            }
                        }
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            fileId.toLong()
                        )
                        return createFileMetaData(uri = contentUri, columnName = "_data")
                    }
                    isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        when (type) {
                            "image" -> {
                                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }
                            "video" -> {
                                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }
                            "audio" -> {
                                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])
                        return createFileMetaData(contentUri, selection, selectionArgs, "_data")
                    }
                    // Online file / cloud
                    else -> {
                        val fileMetaData = createFileMetaData(uri, null, null, null)
                        fileMetaData?.isOnlineFile = true
                        return fileMetaData
                    }
                }
            }
            "content".equals(uri.scheme, true) -> {
                return when {
                    isGooglePhotosUri(uri) -> {
                        val filePath = uri.lastPathSegment ?: return null
                        val fileName = getFileName(filePath)
                        val length = File(filePath).length()
                        FileMetaData(
                            fileName,
                            getFileSize(length),
                            filePath,
                            getFileExtension(filePath),
                            uri.toString(),
                            length
                        )
                    }
                    else -> {
                        val fileMetaData = createFileMetaData(uri, null, null, null)
                        fileMetaData?.isOnlineFile = true
                        return fileMetaData
                    }
                }
            }
            "file".equals(uri.scheme, true) -> {
                val filePath = uri.path ?: return null
                val fileName = getFileName(filePath)
                val length = File(filePath).length()
                return FileMetaData(
                    fileName,
                    getFileSize(length),
                    filePath,
                    getFileExtension(filePath),
                    uri.toString(),
                    length
                )
            }
        }
        return null
    }

    private fun createFileMetaData(
        uri: Uri?, selection: String? = null, selectionArgs: Array<String>? = null,
        columnName: String?
    ): FileMetaData? {
        if (uri == null) return null
        val cursor = context.contentResolver.query(uri, null, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            cursor.use {
                val fileNameWithExt =
                    cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                val length = cursor.getInt(cursor.getColumnIndex(OpenableColumns.SIZE)).toLong()
                val fileSize = getFileSize(length)
                var filePath = ""
                if (columnName != null) {
                    filePath = cursor.getString(cursor.getColumnIndex(columnName))
                }
                val fileExtension = getFileExtension(fileNameWithExt)
                return FileMetaData(
                    getFileName(fileNameWithExt),
                    fileSize,
                    filePath,
                    fileExtension,
                    uri.toString(),
                    length
                )
            }
        }
        return null
    }

    fun writeOnlineFileToCache(fileMetaData: FileMetaData): String? {
        val attachmentCacheDirectoryPath = File(getFileCacheDirectoryPath())
        if (!attachmentCacheDirectoryPath.exists()) {
            attachmentCacheDirectoryPath.mkdir()
        }
        val file = File(attachmentCacheDirectoryPath, fileMetaData.name + "." + fileMetaData.ext)
        val uri = Uri.parse(fileMetaData.uri)
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val outputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output, DEFAULT_BUFFER_SIZE)
            }
        }
        return file.path
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /* private fun isGoogleDriveUri(uri: Uri): Boolean {
         return "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority
     }*/

    private fun getFileCacheDirectoryPath(): String {
        return context.cacheDir.toString() + "/SpiffyFiles/"
    }

    fun deleteCacheDirectory(cacheDirectoryPath: String) {
        val fileDirectory = File(cacheDirectoryPath)
        val listFiles = fileDirectory.listFiles()
        if (!listFiles.isNullOrEmpty()) {
            listFiles.forEach {
                it.delete()
            }
            fileDirectory.delete()
        }
    }

    fun isFileExists(filePath: String): Boolean {
        return File(filePath).exists()
    }
}
