package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.NoUserFoundException
import com.stsf.globalbackend.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired
    private val userRepository: UserRepository
) {
    fun isTeacher(userId: Long): Boolean {
        val user = userRepository.findByIdOrNull(userId) ?: throw NoUserFoundException()

        return user.isTeacher
    }

    fun isStudent(userId: Long): Boolean = !isTeacher(userId)
}