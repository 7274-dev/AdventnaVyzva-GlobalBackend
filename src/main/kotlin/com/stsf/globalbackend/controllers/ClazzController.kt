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

@RestController
@RequestMapping("/api/class")
class ClazzController (@Autowired
                       private val classService: ClassService,
                       @Autowired
                       private val authenticationService: AuthenticationService) {



	@GetMapping("/id")
	fun getClass(@RequestParam classId: Long): GenericResponse<Class> {
		return GenericResponse(classService.getClassById(classId))
	}

	@PutMapping("")
	fun createClassController(@RequestHeader token: String, @RequestParam className: String): GenericResponse<Class> {
		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isAdmin && !authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(classService.createClass(className))

	}

	// Won't this cause problems with classMembers?
	@DeleteMapping("")
	fun deleteClassController(@RequestHeader token: String, @RequestParam classId: Long): GenericResponse<String> {

		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isTeacher && !authenticatedUser.isAdmin) {
			throw InsufficientPermissionsException()
		}

		classService.deleteClassAndOrphanClassMembers(classId)
		return GenericResponse("Ok")

	}

	@GetMapping("")
	fun getAllClassesController(@RequestHeader token: String): GenericResponse<List<Class>> {

		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isTeacher && !authenticatedUser.isAdmin) {
			throw InsufficientPermissionsException()
		}

		val classes = classService.getAllClasses()

		return GenericResponse(classes)
	}

	@PutMapping("/member")
	fun addUserToClassController(@RequestHeader token: String, @RequestBody userAndClassId: UserAndClassId): GenericResponse<ClassMember> {

		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isTeacher && !authenticatedUser.isAdmin) {
			throw InsufficientPermissionsException()
		}

		// TODO: Check if teacher is adding students to his class
		// NOTE: Admins should have the ability to add any user to any class
		return GenericResponse(classService.addUserToClass(userAndClassId.userId, userAndClassId.classId))

	}

	@PatchMapping("")
	fun editClassName(@RequestHeader token: String, @RequestParam className: String, @RequestParam classId: Long): GenericResponse<Class> {
		return GenericResponse(classService.changeClassName(classId, className))
	}

	@DeleteMapping("/member")
	fun removeUserFromClassController(@RequestHeader token: String, @RequestBody userAndClassId: UserAndClassId): GenericResponse<String> {

		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isTeacher && !authenticatedUser.isAdmin) {
			throw InsufficientPermissionsException()
		}

		// TODO: Check if teacher is removing students from his class
		// NOTE: Admins should have the ability to remove any user from any class
		classService.removeUserFromClass(userAndClassId.userId, userAndClassId.classId)

		return GenericResponse("Ok")
	}

	@GetMapping("/member")
	fun getAllUsersFromClassController(@RequestHeader token: String, @RequestParam classId: Long): GenericResponse<List<User>> {
		val authenticatedUser = authenticationService.getUserByToken(token)
		val users = classService.getAllUsersInClass(classId)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}
		// TODO: Check if teacher owns this class
		// NOTE: Admins should have the ability to get users from any class
		return GenericResponse(users)
	}

}
