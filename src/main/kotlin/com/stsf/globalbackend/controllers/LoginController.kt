package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.request.GenericResponse
import com.stsf.globalbackend.services.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/login")
    fun login(@RequestHeader username: String, @RequestHeader password: String): GenericResponse<String> {
        val token = authenticationService.createToken(username, password)

        return GenericResponse(token)
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader token: String): GenericResponse<String> {
        authenticationService.invalidateToken(token)

        return GenericResponse("Ok")
    }
}