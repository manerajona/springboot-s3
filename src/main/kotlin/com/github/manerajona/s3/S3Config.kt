package com.github.manerajona.s3

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(S3Prop::class)
class S3Config(private val properties: S3Prop) {

    @Bean
    fun amazonS3(): AmazonS3 {
        val credentials = BasicAWSCredentials(
            properties.access,
            properties.secret
        )

        return AmazonS3ClientBuilder.standard()
            .withRegion(properties.region)
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build()
    }
}
