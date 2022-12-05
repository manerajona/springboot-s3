package com.github.manerajona.s3

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "s3", ignoreUnknownFields = false)
data class S3Prop(
    val access: String,
    val secret: String,
    val region: String,
    val bucket: String
)
