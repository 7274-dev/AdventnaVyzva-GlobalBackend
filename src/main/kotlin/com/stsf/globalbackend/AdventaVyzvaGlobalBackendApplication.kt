package com.stsf.globalbackend

import com.stsf.globalbackend.configuration.ConfigurationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableConfigurationProperties(ConfigurationProperties::class)
class AdventnaVyzvaGlobalBackendApplication

fun main(args: Array<String>) {
    runApplication<AdventnaVyzvaGlobalBackendApplication>(*args)
}
