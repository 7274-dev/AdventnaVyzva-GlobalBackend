package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.exceptions.NoSuchUserException
import com.stsf.globalbackend.models.User
import com.stsf.globalbackend.request.GenericResponse
import com.stsf.globalbackend.services.AdminService
import com.stsf.globalbackend.services.AuthenticationService
import com.stsf.globalbackend.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController("/api")
class AdminController(
    @Autowired
    private val adminService: AdminService,
    @Autowired
    private val authenticationService: AuthenticationService,
    @Autowired
    private val userService: UserService
) {

    @PutMapping("/student")
    fun createStudent(@RequestHeader token: String, @RequestBody user: com.stsf.globalbackend.request.User): GenericResponse<User> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isTeacher) {
            throw InsufficientPermissionsException()
        }

        val (username, password, name) = user
        return GenericResponse(adminService.createStudentUser(username, password, name))
    }

    @DeleteMapping("/student")
    fun deleteStudent(@RequestHeader token: String, @RequestParam userId: Long): GenericResponse<String> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isTeacher) {
            throw InsufficientPermissionsException()
        }

        if (!userService.isStudent(userId)) {
            throw NoSuchUserException()
        }

        adminService.deleteUser(userId)

        return GenericResponse("Ok")
    }

    @PatchMapping("/student")
    fun changeStudentPassword(@RequestHeader token: String, @RequestParam userId: Long, @RequestParam newPassword: String): GenericResponse<User> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isTeacher) {
            throw InsufficientPermissionsException()
        }

        if (!userService.isStudent(userId)) {
            throw NoSuchUserException()
        }

        return GenericResponse(adminService.changeUserPassword(userId, newPassword))
    }

    @PutMapping("/teacher")
    fun createTeacher(@RequestHeader token: String, @RequestBody user: com.stsf.globalbackend.request.User): GenericResponse<User> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isTeacher) {
            throw InsufficientPermissionsException()
        }

        val (username, password, name) = user
        return GenericResponse(adminService.createTeacherAccount(username, password, name))
    }

    @DeleteMapping("/teacher")
    fun deleteTeacher(@RequestHeader token: String, @RequestParam userId: Long): GenericResponse<String> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isTeacher) {
            throw InsufficientPermissionsException()
        }

        if (!userService.isTeacher(userId)) {
            throw NoSuchUserException()
        }

        adminService.deleteUser(userId)
        return GenericResponse("Ok")
    }

    @PatchMapping("/teacher")
    fun changeTeacherPassword(@RequestHeader token: String, @RequestParam userId: Long, @RequestParam newPassword: String): GenericResponse<User> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isTeacher) {
            throw InsufficientPermissionsException()
        }

        if (!userService.isTeacher(userId)) {
            throw NoSuchUserException()
        }

        return GenericResponse(adminService.changeUserPassword(userId, newPassword))
    }

}