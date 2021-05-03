package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.NoSuchClassException
import com.stsf.globalbackend.models.Homework
import com.stsf.globalbackend.repositories.ClassRepository
import com.stsf.globalbackend.repositories.HomeworkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class HomeworkService (
	@Autowired
	private val homeworkRepository: HomeworkRepository,
	@Autowired
	private val classRepository: ClassRepository
) {


	fun getHomeworkByClass(classId: Long): List<Homework> {
		val clazz = classRepository.findByIdOrNull(classId) ?: throw NoSuchClassException()
		val homework = homeworkRepository.findAllByClazz(clazz)
		val output = mutableListOf<Homework>()

		val today = Date.from(LocalDateTime.now()
			.atZone(ZoneId.systemDefault())
			.toInstant())

		for (hw in homework) {

			if (hw.fromDate.after(today)) {
				output.add(hw)
			}

		}

		return output

	}

	fun getHomeworkByDateAndClass(classId: Long, date: Date): List<Homework> {
		val clazz = classRepository.findByIdOrNull(classId) ?: throw NoSuchClassException()

		val homework = homeworkRepository.findAllByClazz(clazz)
		val output: MutableList<Homework> = mutableListOf()

		val today = Date.from(LocalDateTime.now()
			.atZone(ZoneId.systemDefault())
			.toInstant())

		for (hw in homework) {
			if (hw.due.before(date) && hw.fromDate.after(today)) {
				output.add(hw)
			}
		}

		return output
	}

	fun createHomework(newHomework: com.stsf.globalbackend.request.Homework): Homework {
		val (classId, title, text, due, from) = newHomework

		val clazz = classRepository.findByIdOrNull(classId) ?: throw NoSuchClassException()

		val homework = Homework(-1, clazz, title, text, due, from)

		return homeworkRepository.save(homework)
	}

	fun deleteHomework(homeworkId: Long) {
		homeworkRepository.deleteById(homeworkId)
	}

}