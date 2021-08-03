package com.stsf.globalbackend.controllers


import com.fasterxml.jackson.annotation.JsonFormat
import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.request.*
import com.stsf.globalbackend.services.AuthenticationService
import com.stsf.globalbackend.services.HomeworkService
import com.stsf.globalbackend.services.MarkdownService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/homework")
class HomeworkController (
	@Autowired
	private val authenticationService: AuthenticationService,
	@Autowired
	private val homeworkService: HomeworkService,
	@Autowired
	private val markdownService: MarkdownService
) {

	@GetMapping("/admin")
	fun getHomeworkForAdmin(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isAdmin) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(homeworkService.getHomeworkData(homeworkId))
	}

	@PutMapping("/")
	fun addHomework(@RequestHeader token: String, @RequestBody homework: Homework): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		homework.text = markdownService.markdownToHTML(homework.text)

		return GenericResponse(homeworkService.createHomework(homework))
	}

	@DeleteMapping("/")
	fun deleteHomework(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<String> {
		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		homeworkService.deleteHomework(homeworkId)

		return GenericResponse("Ok")
	}

	// FIXME
	@PatchMapping("/")
	fun editHomework(@RequestHeader token: String, @RequestBody homework: Homework): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authenticatedUser = authenticationService.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		homework.text = markdownService.markdownToHTML(homework.text)

		return GenericResponse(homeworkService.createHomework(homework))

	}

	@GetMapping("/class")
	fun getHomeworkForClass(@RequestHeader token: String, @RequestParam classId: Long): GenericResponse<List<com.stsf.globalbackend.models.Homework>> {
		// TODO: Add checks to see if user has access to this homework
		return GenericResponse(homeworkService.getHomeworkByClass(classId))
	}

	@GetMapping("/student")
	fun getHomeworkForStudent(@RequestHeader token: String, @RequestParam userId: Long): GenericResponse<List<com.stsf.globalbackend.models.Homework>> {
		// TODO: Same here
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
			return GenericResponse(homeworkService.getHomeworkByDateAndClass(classId, date))
	}
}