package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    @Autowired
    private val authenticationService: AuthenticationService
    ) {

    @GetMapping("/test")
    fun test(): String {
        authenticationService.createToken("a", "b")
        return "It works!"
    }
}