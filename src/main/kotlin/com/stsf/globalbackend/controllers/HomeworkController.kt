package com.stsf.globalbackend.controllers


import com.fasterxml.jackson.annotation.JsonFormat
import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.request.*
import com.stsf.globalbackend.services.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/homework")
class HomeworkController (
	@Autowired
	private val auth: AuthenticationService,
	@Autowired
	private val homeworkService: HomeworkService,
	@Autowired
	private val markdownService: MarkdownService,
	@Autowired
	private val classService: ClassService,
) {

	@GetMapping("/admin")
	fun getHomeworkForAdmin(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isAdmin) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(homeworkService.getHomeworkData(homeworkId))
	}

	@PutMapping("/")
	fun addHomework(@RequestHeader token: String, @RequestBody homework: Homework): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		homework.text = markdownService.markdownToHTML(homework.text)

		return GenericResponse(homeworkService.createHomework(homework))
	}

	@DeleteMapping("/")
	fun deleteHomework(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<String> {
		val authenticatedUser = auth.getUserByToken(token)

		var userHasAccessToHomework = classService.getAllUsersInClass(homeworkService.getHomeworkData(homeworkId).clazz.id)
			.contains(authenticatedUser)

		if (authenticatedUser.isAdmin) {
			userHasAccessToHomework = true
		}

		if (!authenticatedUser.isTeacher || !userHasAccessToHomework) {
			throw InsufficientPermissionsException()
		}
		// DONE: Check if teacher owns the class that the homework is assigned to
		homeworkService.deleteHomework(homeworkId)
		return GenericResponse("Ok")
	}

	// FIXME
	@PatchMapping("/")
	fun editHomework(@RequestHeader token: String, @RequestBody homework: Homework): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}
		// TODO: Check if teacher owns the class that the homework is assigned to
		homework.text = markdownService.markdownToHTML(homework.text)

		return GenericResponse(homeworkService.createHomework(homework))

	}

	@GetMapping("/class")
	fun getHomeworkForClass(@RequestHeader token: String, @RequestParam classId: Long): GenericResponse<List<com.stsf.globalbackend.models.Homework>> {
		val authenticatedUser = auth.getUserByToken(token)
		var userHasAccessToHomework = classService.getAllUsersInClass(classId).contains(authenticatedUser)

		if (authenticatedUser.isAdmin) {
			userHasAccessToHomework = true
		}

		if (!userHasAccessToHomework) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(homeworkService.getHomeworkByClass(classId))
	}

	@GetMapping("/student")
	fun getHomeworkForStudent(@RequestHeader token: String, @RequestParam userId: Long): GenericResponse<List<com.stsf.globalbackend.models.Homework>> {
		val authenticatedUser = auth.getUserByToken(token)

		// Do we want to allow admins to view homeworks also on this URI?
		if (authenticatedUser.id != userId) {
			throw InsufficientPermissionsException()
		}
		return GenericResponse(homeworkService.getAllHomeworksByStudent(userId))
	}

	@GetMapping("/date")
	fun getHomeworkByDateAndClass(
		@RequestHeader
		token: String,
		@RequestParam @JsonFormat(pattern = "dd-MM-yy")
		date: Date,
		@RequestParam
		classId: Long
	): GenericResponse<List<com.stsf.globalbackend.models.Homework>> {
		val authenticatedUser = auth.getUserByToken(token)
		var userHasAccessToHomework = classService.getAllUsersInClass(classId).contains(authenticatedUser)

		if (authenticatedUser.isAdmin)  {
			userHasAccessToHomework = true
		}

		if (!userHasAccessToHomework) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(homeworkService.getHomeworkByDateAndClass(classId, date))
	}
}