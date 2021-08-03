package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.exceptions.NoSuchUserException
import com.stsf.globalbackend.models.User
import com.stsf.globalbackend.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.random.Random
import kotlin.random.nextInt

@Service
class AdminService(
    @Autowired
    private val userRepository: UserRepository
) {

    fun getUserPassword(userId: Long): String { // :(((
        val targetUser = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()

        if (targetUser.isTeacher) {
            throw InsufficientPermissionsException()
        }

        return targetUser.password
    }

    private fun generatePassword(length: Int): String {
        val characters = "ABCDEFHIJKLMNOPQRSTUVWXYZ".toCharArray()
        val digits = "1234567890".toCharArray()

        val output = StringBuilder()

        for (i in 0..length) {
            if (i % 2 == 0) {
                val newCharacterIndex = Random.nextInt(0..characters.size)
                output.append(characters[newCharacterIndex])
            }
            else {
                val newCharacterIndex = Random.nextInt(0..digits.size)
                output.append(digits[newCharacterIndex])
            }
        }

        return output.toString()
    }

    fun isUserDatabaseEmpty(): Boolean {
        return userRepository.count() == 0L
    }

    fun changeUserPassword(userId: Long, newPassword: String? = null): User {
        var newPassword = newPassword
        if (newPassword == null) {
            newPassword = generatePassword(8)
        }

        val targetUser = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()

        targetUser.password = newPassword

        return userRepository.save(targetUser)
    }

    private fun createUser(username: String, password: String? = null, name: String, isAdmin: Boolean, isTeacher: Boolean): User { // Maybe some more elegant solution
        var newPassword = password
        if (newPassword == null) {
            newPassword = generatePassword(8)
        }

        val newUser = User(-1, username, newPassword, name, isTeacher, isAdmin)

        return userRepository.save(newUser)
    }

    fun createStudentUser(username: String, password: String? = null, name: String): User {
        return createUser(username, password, name, false, false)
    }

    fun createTeacherAccount(username: String, password: String? = null, name: String): User {
        return createUser(username, password, name, false, true)
    }

    fun createAdminAccount(username: String, password: String? = null, name: String): User {
        return createUser(username, password, name, true, true)
    }

    fun deleteUser(userId: Long) {
        userRepository.deleteById(userId)
    }

    fun getStudentData(studentID: Long): User {
        return userRepository.findByIdOrNull(studentID) ?: throw NoSuchUserException()
    }
}
