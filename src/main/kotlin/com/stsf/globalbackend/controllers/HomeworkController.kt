package com.stsf.globalbackend.controllers


import com.fasterxml.jackson.annotation.JsonFormat
import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.request.*
import com.stsf.globalbackend.services.AuthenticationService
import com.stsf.globalbackend.services.ClassService
import com.stsf.globalbackend.services.HomeworkService
import com.stsf.globalbackend.services.MarkdownService
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
	private val classService: ClassService
) {

	@PostMapping("/mdtohtml")
	fun getHtmlFromMarkdown(@RequestHeader token: String, @RequestBody markdown: String): GenericResponse<String> {
		return GenericResponse(markdownService.markdownToHTML(markdownService.htmlEncode(markdown)))
	}

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

		return GenericResponse(homeworkService.createHomework(homework))
	}

	@DeleteMapping("/")
	fun deleteHomework(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<String> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}
		// TODO: Check if teacher owns the class that the homework is assigned to
		homeworkService.deleteHomework(homeworkId)

		return GenericResponse("Ok")
	}

	// Should be fixed? (I didn't test it)
	// - Ivan
	@PatchMapping("/")
	fun editHomework(@RequestHeader token: String, @RequestParam homeworkId: Long, @RequestBody homework: Homework): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		val oldHomework = homeworkService.getHomeworkById(homeworkId)

		oldHomework.clazz = classService.getClassById(homework.classId)
		oldHomework.due = homework.due
		oldHomework.fromDate = homework.fromDate
		oldHomework.text = homework.text
		oldHomework.title = homework.title

		return GenericResponse(homeworkService.replaceHomework(oldHomework))
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
			// TODO: Check if user has access to this homework
			return GenericResponse(homeworkService.getHomeworkByDateAndClass(classId, date))
	}
}