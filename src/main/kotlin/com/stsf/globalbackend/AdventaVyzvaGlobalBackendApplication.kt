package com.stsf.globalbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class AdventaVyzvaGlobalBackendApplication

fun main(args: Array<String>) {
    runApplication<AdventaVyzvaGlobalBackendApplication>(*args)
}
