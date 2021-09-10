package com.stsf.globalbackend.controllers


import com.fasterxml.jackson.annotation.JsonFormat
import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.exceptions.NoSuchHomeworkException
import com.stsf.globalbackend.repositories.HomeworkRepository
import com.stsf.globalbackend.request.*
import com.stsf.globalbackend.services.AuthenticationService
import com.stsf.globalbackend.services.HomeworkService
import com.stsf.globalbackend.services.MarkdownService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
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
	private val homeworkRepository: HomeworkRepository,
) {

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

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

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

		homework.text = markdownService.markdownToHTML(homework.text)

		return GenericResponse(homeworkService.createHomework(homework))

	}

	@GetMapping("/class")
	fun getHomeworkForClass(@RequestHeader token: String, @RequestParam classId: Long): GenericResponse<List<com.stsf.globalbackend.models.Homework>> {
		// Shouldn't this check if the user is in class or if the user is a teacher?
		return GenericResponse(homeworkService.getHomeworkByClass(classId))
	}

	@GetMapping("/student")
	fun getHomeworkForStudent(@RequestHeader token: String, @RequestParam userId: Long): GenericResponse<List<com.stsf.globalbackend.models.Homework>> {
		// Shouldn't this check if the user is in class or if the user is a teacher?
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
	@PostMapping("/submit")
	fun submitHomework(@RequestHeader token: String, @RequestBody homeworkSubmission: HomeworkSubmission): GenericResponse<String> {
		val authenticatedUser = auth.getUserByToken(token)

		if (homeworkRepository.findByIdOrNull(homeworkSubmission.homeworkId) !in homeworkService.getAllHomeworksByStudent(authenticatedUser.id)) {
			throw InsufficientPermissionsException()
		}

		if (homeworkSubmission.content == null) {
			homeworkSubmission.content = ""
		}

		val homework = homeworkRepository.findByIdOrNull(homeworkSubmission.homeworkId) ?: throw NoSuchHomeworkException()

		homeworkService.submitHomework(com.stsf.globalbackend.models.HomeworkSubmission(-1, homework, authenticatedUser, homeworkSubmission.content), homeworkSubmission.fileIds)

		return GenericResponse("Ok")

	}
}