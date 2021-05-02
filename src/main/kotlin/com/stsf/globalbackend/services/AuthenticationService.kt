package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.BadCredentialsException
import com.stsf.globalbackend.exceptions.BadTokenException
import com.stsf.globalbackend.exceptions.NoUserFoundException
import com.stsf.globalbackend.interceptors.AuthenticationInterceptor
import com.stsf.globalbackend.models.User
import com.stsf.globalbackend.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    @Autowired
    private val userRepository: UserRepository
) {
    fun createToken(username: String, password: String): String {
        val users = userRepository.getUsersByUsername(username)

        var targetUser: User? = null

        for (user in users) {
            if (user.password == password) {
                targetUser = user
                break
            }
        }

        if (targetUser == null) {
            throw BadCredentialsException()
        }

        var token: String = UUID.randomUUID().toString()
        while (AuthenticationInterceptor.tokenExists(token)) {
            token = UUID.randomUUID().toString()
        }
        AuthenticationInterceptor.addToken(token, targetUser.id)

        return token
    }

    fun invalidateToken(token: String) {
        AuthenticationInterceptor.invalidateToken(token)
    }

    fun getUserByToken(token: String): User {
        val userId = AuthenticationInterceptor.getUserIdByToken(token) ?: throw BadTokenException()

        return userRepository.findByIdOrNull(userId) ?: throw NoUserFoundException()
    }
}