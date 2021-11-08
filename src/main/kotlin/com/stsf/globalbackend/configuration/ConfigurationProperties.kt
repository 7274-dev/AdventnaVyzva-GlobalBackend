package com.stsf.globalbackend.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "stsf.config")
class ConfigurationProperties(
    var storageDirectory: String = "./files/"
)