package com.github.manerajona.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.*

@Component
class S3Client(
    private val amazonS3: AmazonS3,
    private val properties: S3Prop
) {

    private val log = KotlinLogging.logger {}

    fun uploadObject(multipartFile: MultipartFile): URL {
        val id = UUID.randomUUID().toString()
        upload(id, multipartFile)
        return amazonS3.getUrl(properties.bucket, id)
    }

    fun deleteObject(id: String) =
        try {
            amazonS3.deleteObject(
                DeleteObjectRequest(properties.bucket, id)
            )
        } catch (e: Exception) {
            log.error("Error deleting object", e)
            throw RuntimeException(e)
        }

    fun modifyObject(id: String, multipartFile: MultipartFile): URL {
        if (amazonS3.doesObjectExist(properties.bucket, id)) {
            deleteObject(id)
        }
        upload(id, multipartFile)
        return amazonS3.getUrl(properties.bucket, id)
    }

    private fun upload(id: String, multipartFile: MultipartFile) =
        try {
            val file = multipartFileToFile(multipartFile)
            amazonS3.putObject(
                PutObjectRequest(properties.bucket, id, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
            file.delete()
        } catch (e: Exception) {
            log.error("Error uploading object", e)
            throw RuntimeException(e)
        }

    private fun multipartFileToFile(multipartFile: MultipartFile): File =
        try {
            val file = File(multipartFile.originalFilename!!)
            FileOutputStream(file).use {
                val fos = FileOutputStream(file)
                fos.write(multipartFile.bytes)
                fos.close()
                return file
            }
        } catch (e: IOException) {
            log.error("Error converting file", e)
            throw RuntimeException(e)
        }

}
