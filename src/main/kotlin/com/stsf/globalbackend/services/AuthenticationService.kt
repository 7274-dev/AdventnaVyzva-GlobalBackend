package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.BadCredentialsException
import com.stsf.globalbackend.interceptors.AuthenticationInterceptor
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService {
    fun createToken(username: String, password: String): String {
        // TODO: verify username and password from db

//        if (incorrectCredentials) {
//            throw BadCredentialsException()
//        }

        var token: String = UUID.randomUUID().toString()
        while (AuthenticationInterceptor.tokenExists(token)) {
            token = UUID.randomUUID().toString()
        }
        AuthenticationInterceptor.addToken(token, -1) // TODO: add userid fetched from db

        return token
    }

    fun invalidateToken(token: String) {
        AuthenticationInterceptor.invalidateToken(token)
    }

    // TODO: add methods to get User from token
}