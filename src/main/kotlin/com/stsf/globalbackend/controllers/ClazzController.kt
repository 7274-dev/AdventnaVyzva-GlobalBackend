package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.ClassMember
import com.stsf.globalbackend.models.User
import com.stsf.globalbackend.request.GenericResponse
import com.stsf.globalbackend.request.UserAndClassId
import com.stsf.globalbackend.services.AuthenticationService
import com.stsf.globalbackend.services.ClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController("/api")
class ClazzController (@Autowired
                       private val classService: ClassService,
                       @Autowired
                       private val authenticationService: AuthenticationService) {


	@PutMapping("/class")
	fun createClassController(@RequestHeader token: String, @RequestParam className: String): GenericResponse<Class> {

		val authenticatedUser = authenticationService.getUserByToken(token)
		if (!authenticatedUser.isTeacher)
		{
			throw InsufficientPermissionsException()
		}

		return GenericResponse(classService.createClass(className))

	}

	// Won't this cause problems with classMembers?
	@DeleteMapping("/class")
	fun deleteClassController(@RequestHeader token: String, @RequestParam classId: Long): GenericResponse<String> {

		val authenticatedUser = authenticationService.getUserByToken(token)
		var isUserInClass = classService.getAllUsersInClass(classId).contains(authenticatedUser)

		if (authenticatedUser.isAdmin) {
			isUserInClass = true
		}

		if (!authenticatedUser.isTeacher && !isUserInClass) {
			throw InsufficientPermissionsException()
		}

		classService.deleteClass(classId)
		return GenericResponse("Ok")

	}

//	@GetMapping("/class")
//	fun getAllClassesController(@RequestHeader token: String): GenericResponse<List<Class>> {
//
//		val authenticatedUser = authenticationService.getUserByToken(token)
//		val classes = classService.getAllClasses()
//
//		if (!authenticatedUser.isTeacher) {
//			throw InsufficientPermissionsException()
//		}
//		return GenericResponse(classes)
//	}

	@PutMapping("/classMember")
	fun addUserToClassController(@RequestHeader token: String, @RequestBody userAndClassId: UserAndClassId): GenericResponse<ClassMember> {
		// DONE: Check if teacher is adding students to his class
		// NOTE: Admins should have the ability to add any user to any class

		val authenticatedUser = authenticationService.getUserByToken(token)
		var isUserInClass = classService.getAllUsersInClass(userAndClassId.classId).contains(authenticatedUser)
		val isClassEmpty = classService.getAllUsersInClass(userAndClassId.classId).isEmpty()

		if (authenticatedUser.isAdmin) {
			isUserInClass = true
		}

		if ((!authenticatedUser.isTeacher && !isUserInClass) || !isClassEmpty) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(classService.addUserToClass(userAndClassId.userId, userAndClassId.classId))
	}

	@DeleteMapping("/classMember")
	fun removeUserFromClassController(@RequestHeader token: String, @RequestBody userAndClassId: UserAndClassId): GenericResponse<String> {
		// DONE: Check if teacher is removing students from his class
		// NOTE: Admins should have the ability to remove any user from any class

		val authenticatedUser = authenticationService.getUserByToken(token)
		var isUserInClass = classService.getAllUsersInClass(userAndClassId.classId).contains(authenticatedUser)

		if (authenticatedUser.isAdmin) {
			isUserInClass = true
		}

		if (!authenticatedUser.isTeacher && !isUserInClass) {
			throw InsufficientPermissionsException()
		}

		classService.removeUserFromClass(userAndClassId.userId, userAndClassId.classId)
		return GenericResponse("Ok")
	}

	@GetMapping("/classMember")
	fun getAllUsersFromClassController(@RequestHeader token: String, @RequestParam classId: Long): GenericResponse<List<User>> {
		// DONE: Check if teacher owns this class
		// NOTE: Admins should have the ability to get users from any class

		val authenticatedUser = authenticationService.getUserByToken(token)
		var isUserInClass = classService.getAllUsersInClass(classId).contains(authenticatedUser)

		if (authenticatedUser.isAdmin) {
			isUserInClass = true
		}

		if (!authenticatedUser.isTeacher && !isUserInClass) {
			throw InsufficientPermissionsException()
		}

		val users = classService.getAllUsersInClass(classId)
		return GenericResponse(users)
	}

}
