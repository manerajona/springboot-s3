package com.github.manerajona.s3

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/s3/files")
class S3Controller(private val client: S3Client) {

    @PostMapping(headers = ["content-type=multipart/*"])
    fun upload(
        @RequestPart(value = "file") file: MultipartFile
    ): ResponseEntity<String> {
        val url = client.uploadObject(file)
        val location = url.toURI()

        return ResponseEntity
            .created(location)
            .build()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable id: String
    ) = client.deleteObject(id)

    @PutMapping("/{id}", headers = ["content-type=multipart/*"])
    fun modify(
        @PathVariable id: String,
        @RequestPart(value = "file") file: MultipartFile
    ): ResponseEntity<String> {
        val url = client.modifyObject(id, file)
        val location = url.toURI()

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .location(location)
            .build()
    }
}
