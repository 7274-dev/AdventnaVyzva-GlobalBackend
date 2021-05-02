package com.stsf.globalbackend.controllers


import com.stsf.globalbackend.exceptions.BadTokenException
import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.repositories.ClassRepository
import com.stsf.globalbackend.request.Date
import com.stsf.globalbackend.request.GenericResponse
import com.stsf.globalbackend.request.Homework
import com.stsf.globalbackend.request.UpdatedHomework
import com.stsf.globalbackend.services.AuthenticationService
import com.stsf.globalbackend.services.HomeworkService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat

@RestController("/api")
class HomeworkController (
	@Autowired
	private val auth: AuthenticationService,
	@Autowired
	private val homeworkService: HomeworkService,
	@Autowired
	private val classRepository: ClassRepository,
		) {

	@PutMapping("/homework")
	fun addHomework(@RequestHeader token: String, @RequestBody homework: Homework): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authentificatedUser = auth.getUserByToken(token)

		if (!authentificatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		} else {
			return GenericResponse(homeworkService.createNewHomework(classRepository.findClassById(homework.classId),
									homework.title, homework.text,
									SimpleDateFormat("dd-MM-yy HH:mm:ss").parse(homework.due.toString()),
									SimpleDateFormat("dd-MM-yy HH:mm:ss").parse(homework.due.toString())))
		}
	}

	@DeleteMapping("/homework")
	fun deleteHomework(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<String> {
		return GenericResponse(homeworkService.deleteHomework(homeworkId))
	}

	@PatchMapping("/homework")
	fun editHomework(@RequestHeader token: String, @RequestBody homework: UpdatedHomework): GenericResponse<com.stsf.globalbackend.models.Homework> {

		return GenericResponse(homeworkService.createNewHomework(classRepository.findClassById(homework.classId),
								homework.title, homework.text,
								SimpleDateFormat("dd-MM-yy HH:mm:ss").parse(homework.due.toString()),
								SimpleDateFormat("dd-MM-yy HH:mm:ss").parse(homework.from.toString())))

	}

	@GetMapping("/homework/class")
	fun getAllHomeworksForClassById(@RequestHeader token: String, @RequestParam classId: Long): GenericResponse<List<com.stsf.globalbackend.models.Homework>> {
		if (auth.tokenExists(token)) {
		return GenericResponse(homeworkService.getAllHomeworksByClass(classRepository.findClassById(classId)))
		} else throw BadTokenException()
	}

	@GetMapping("/homework/due")
	fun getAllByDateForClass(@RequestHeader token: String, @RequestParam classId: Long, @RequestBody date: Date): GenericResponse<List<com.stsf.globalbackend.models.Homework>> {
		if (auth.tokenExists(token)) {

			return GenericResponse(homeworkService.getAllHomeworksByDateForClass(classRepository.findClassById(classId),
				SimpleDateFormat("dd-MM-yy HH:mm:ss").parse(date.date.toString())))

		} else throw BadTokenException()
	}
}