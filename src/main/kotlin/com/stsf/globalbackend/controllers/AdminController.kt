package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.exceptions.BadTokenException
import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.exceptions.NoSuchUserException
import com.stsf.globalbackend.models.User
import com.stsf.globalbackend.repositories.UserRepository
import com.stsf.globalbackend.request.GenericResponse
import com.stsf.globalbackend.services.AdminService
import com.stsf.globalbackend.services.AuthenticationService
import com.stsf.globalbackend.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController(
    @Autowired
    private val adminService: AdminService,
    @Autowired
    private val authenticationService: AuthenticationService,
    @Autowired
    private val userService: UserService,
    @Autowired
    private val userRepository: UserRepository,
) {

    @GetMapping("/student")
    fun getStudentData(@RequestHeader token: String, @RequestParam studentId: Long): GenericResponse<User> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isAdmin) {
            throw InsufficientPermissionsException()
        }

        val student = adminService.getStudentData(studentId)
        if (student.isTeacher || student.isAdmin) {
            throw InsufficientPermissionsException()
        }

        return GenericResponse(student)
    }
    // TODO: Add a check against a db of most used passwords
    @PutMapping("/student")
    fun createStudent(@RequestHeader token: String, @RequestBody user: com.stsf.globalbackend.request.User): GenericResponse<User> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isAdmin) {
            throw InsufficientPermissionsException()
        }

        val (username, password, name) = user
        return GenericResponse(adminService.createStudentUser(username, password, name))
    }

    @DeleteMapping("/student")
    fun deleteStudent(@RequestHeader token: String, @RequestParam userId: Long): GenericResponse<String> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isAdmin) {
            throw InsufficientPermissionsException()
        }

        if (!userService.isStudent(userId)) {
            throw NoSuchUserException()
        }

        adminService.deleteUser(userId)
        return GenericResponse("Ok")
    }

//     This will be done through editStudent
//    // TODO: Add check against a db of most used passwords
//    @PatchMapping("/student")
//    fun changeStudentPassword(@RequestHeader token: String, @RequestParam userId: Long, @RequestParam newPassword: String): GenericResponse<User> {
//        val authenticatedUser = authenticationService.getUserByToken(token)
//
//        if (!authenticatedUser.isAdmin) {
//            throw InsufficientPermissionsException()
//        }
//
//        if (!userService.isStudent(userId)) {
//            throw NoSuchUserException()
//        }
//
//        return GenericResponse(adminService.changeUserPassword(userId, newPassword))
//    }

    @PatchMapping("/student")
    fun editStudent(@RequestHeader token: String, @RequestBody userName: String, @RequestBody name: String, @RequestBody password: String?, @RequestBody userId: Long): GenericResponse<User> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isAdmin ) {
            throw InsufficientPermissionsException()
        }

        val user = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()
        return if (password == null) {
            GenericResponse(userRepository.save(User(-1, userName, user.password, name, false, false)))
        } else {
            GenericResponse(userRepository.save(User(-1, userName, password, name, false, false)))
        }
    }

    // TODO: Add a check against a db of most used passwords
    @PutMapping("/teacher")
    fun createTeacher(@RequestHeader token: String, @RequestBody user: com.stsf.globalbackend.request.User): GenericResponse<User> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isAdmin) {
            throw InsufficientPermissionsException()
        }

        val (username, password, name) = user
        return GenericResponse(adminService.createTeacherAccount(username, password, name))
    }

    @DeleteMapping("/teacher")
    fun deleteTeacher(@RequestHeader token: String, @RequestParam userId: Long): GenericResponse<String> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isAdmin) {
            throw InsufficientPermissionsException()
        }

        if (!userService.isTeacher(userId)) {
            throw NoSuchUserException()
        }

        adminService.deleteUser(userId)
        return GenericResponse("Ok")
    }
//    This will be dont through editTeacher
//    // TODO: Add a check against a db of most used passwords
//    @PatchMapping("/teacher")
//    fun changeTeacherPassword(@RequestHeader token: String, @RequestParam userId: Long, @RequestParam newPassword: String): GenericResponse<User> {
//        val authenticatedUser = authenticationService.getUserByToken(token)
//
//        if (!authenticatedUser.isAdmin) {
//            throw InsufficientPermissionsException()
//        }
//
//        if (!userService.isTeacher(userId)) {
//            throw NoSuchUserException()
//        }
//
//        return GenericResponse(adminService.changeUserPassword(userId, newPassword))
//    }
    @PatchMapping("/teacher")
    fun editTeacher(@RequestHeader token: String, @RequestBody userName: String, @RequestBody name: String, @RequestBody password: String?, @RequestBody userId: Long): GenericResponse<User> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isAdmin) {
            throw InsufficientPermissionsException()
        }

        val user = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()
    return if (password == null) {
        GenericResponse(userRepository.save(User(-1, userName, user.password, name, true, false)))
    } else {
        GenericResponse(userRepository.save(User(-1, userName, password, name, true, false)))
    }

    }

    // TODO: Add a check against a db of most used passwords
    @PutMapping("/create")
    fun createAdmin(@RequestHeader token: String?, @RequestBody user: com.stsf.globalbackend.request.User): GenericResponse<User> {

        if (!adminService.isUserDatabaseEmpty()) {
            if (token == null) {
                throw BadTokenException()
            }
            val authenticatedUser = authenticationService.getUserByToken(token)

            if (!authenticatedUser.isAdmin) {
                throw InsufficientPermissionsException()
            }
        }


        val (username, password, name) = user
        return GenericResponse(adminService.createAdminAccount(username, password, name))
    }

    @DeleteMapping("/delete")
    fun deleteAdmin(@RequestHeader token: String, @RequestParam userId: Long): GenericResponse<String> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isAdmin) {
            throw InsufficientPermissionsException()
        }

        if (!userService.isAdmin(userId)) {
            throw NoSuchUserException()
        }

        adminService.deleteUser(userId)
        return GenericResponse("Ok")
    }
//    This will be done through editAdmin
//    // TODO: Add a check against a db of most used passwords
//    @PatchMapping("/change")
//    fun changeAdminPassword(@RequestHeader token: String, @RequestParam userId: Long, @RequestParam newPassword: String): GenericResponse<User> {
//        val authenticatedUser = authenticationService.getUserByToken(token)
//
//        if (!authenticatedUser.isAdmin) {
//            throw InsufficientPermissionsException()
//        }
//
//        if (!userService.isAdmin(userId)) {
//            throw NoSuchUserException()
//        }
//
//        return GenericResponse(adminService.changeUserPassword(userId, newPassword))
//    }
    @PatchMapping("/edit")
    fun editAdmin(@RequestHeader token: String, @RequestBody userName: String, @RequestBody name: String, @RequestBody password: String?, @RequestBody userId: Long): GenericResponse<User> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (authenticatedUser.id != userId || !authenticatedUser.isAdmin) {
            throw InsufficientPermissionsException()
        }

        val user = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()
    return if (password == null) {
        GenericResponse(userRepository.save(User(-1, userName, user.password, name, true, true)))
    } else {
        GenericResponse(userRepository.save(User(-1, userName, password, name, true, true)))
    }

    }
}
