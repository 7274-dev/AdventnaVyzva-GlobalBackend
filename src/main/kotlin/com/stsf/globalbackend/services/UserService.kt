package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.NoSuchUserException
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
        val user = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()

        return user.isTeacher
    }

    fun isAdmin(userId: Long): Boolean {
        val user = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()

        return user.isAdmin
    }

    fun isStudent(userId: Long): Boolean = !isTeacher(userId)

    fun findAllUsersNotInClass(): List<Long> = userRepository.findAllUsersNotInClass()
}
