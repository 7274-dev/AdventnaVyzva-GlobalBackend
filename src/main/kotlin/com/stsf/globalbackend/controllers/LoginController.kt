package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.models.User
import com.stsf.globalbackend.request.GenericResponse
import com.stsf.globalbackend.services.AdminService
import com.stsf.globalbackend.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    @Autowired
    private val authenticationService: AuthenticationService,
    @Autowired
    private val adminService: AdminService
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

    // STOPSHIP: remove this!!!!
    @PostMapping("/register")
    fun register(@RequestHeader username: String, @RequestHeader password: String, @RequestHeader name: String): GenericResponse<User> {
        return GenericResponse(adminService.createStudentUser(username, password, name))
    }
}