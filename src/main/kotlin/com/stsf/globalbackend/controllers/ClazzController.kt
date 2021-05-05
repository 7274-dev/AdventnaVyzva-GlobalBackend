package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.ClassMember
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

	// Wont this cause problems?
	@DeleteMapping("/class")
	fun deleteClassController(@RequestHeader token: String, @RequestParam classId: Long): GenericResponse<String> {

		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		classService.deleteClass(classId)
		return GenericResponse("Ok")

	}

	@PutMapping("/classMember")
	fun addUserToClassController(@RequestHeader token: String, @RequestBody userAndClassId: UserAndClassId): GenericResponse<ClassMember> {

		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(classService.addUserToClass(userAndClassId.userId, userAndClassId.classId))

	}

	@DeleteMapping("/classMember")
	fun removeUserFromClass(@RequestHeader token: String, @RequestBody userAndClassId: UserAndClassId): GenericResponse<String> {

		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		classService.removeUserFromClass(userAndClassId.userId, userAndClassId.classId)

		return GenericResponse("Ok")
	}

}
