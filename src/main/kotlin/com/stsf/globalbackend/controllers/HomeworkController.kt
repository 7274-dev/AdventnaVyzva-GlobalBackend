package com.stsf.globalbackend.controllers


import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.request.*
import com.stsf.globalbackend.services.AuthenticationService
import com.stsf.globalbackend.services.HomeworkService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController("/api/homework")
class HomeworkController (
	@Autowired
	private val auth: AuthenticationService,
	@Autowired
	private val homeworkService: HomeworkService
) {

	@PutMapping
	fun addHomework(@RequestHeader token: String, @RequestBody homework: Homework): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(homeworkService.createHomework(homework))
	}

	@DeleteMapping
	fun deleteHomework(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<String> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		homeworkService.deleteHomework(homeworkId)

		return GenericResponse("Ok")
	}

	@PatchMapping
	fun editHomework(@RequestHeader token: String, @RequestBody homework: Homework): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(homeworkService.createHomework(homework))

	}

	@GetMapping("/class")
	fun getHomeworkForClass(@RequestHeader token: String, @RequestParam classId: Long): GenericResponse<List<com.stsf.globalbackend.models.Homework>> {
		return GenericResponse(homeworkService.getHomeworkByClass(classId))
	}

	@GetMapping("/due")
	fun getHomeworkByDateAndClass(@RequestHeader token: String, @RequestBody dateAndClassId: DateAndClassId): GenericResponse<List<com.stsf.globalbackend.models.Homework>> {
			return GenericResponse(homeworkService.getHomeworkByDateAndClass(dateAndClassId.classId, dateAndClassId.date))
	}
}