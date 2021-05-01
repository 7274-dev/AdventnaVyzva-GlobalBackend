package com.stsf.globalbackend.services

import com.stsf.globalbackend.interceptors.AuthenticationInterceptor
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService {
    fun createToken(username: String, password: String): String? {
        // TODO: verify username and password from db
        val token: String = UUID.randomUUID().toString()

        AuthenticationInterceptor.addToken(token, -1) // TODO: add userid fetched from db

        return token
    }
}