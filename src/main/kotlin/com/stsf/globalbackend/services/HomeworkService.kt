package com.stsf.globalbackend.services

import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.Homework
import com.stsf.globalbackend.repositories.HomeworkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class HomeworkService (
	@Autowired
	private val homeworkRepository: HomeworkRepository,
	) {

	// Get all homeworks by class name
	fun getAllHomeworksByClass(clazz: Class): List<Homework> {
		return homeworkRepository.findAllByClass(clazz)
	}

	// Get all homeworks by date
	fun getAllHomeworksByDateForClass(clazz: Class, date: Date): List<Homework> {
		var rawHomeworks = homeworkRepository.findAllByClass(clazz)
		var outHomework = mutableListOf<Homework>()

		for (homework in rawHomeworks) {
			if (homework.due.before(date)) {
				outHomework.add(homework)
			}
		}

		return outHomework
	}

	// Crate new homework for class
	fun createNewHomework(clazz: Class, title: String, text: String, due: Date, from: Date): Homework {
		val homework = Homework(-1, clazz, title, text, due, from)
		homeworkRepository.save(homework)
		return homework
	}

	fun deleteHomework(homeworkId: Long): String {
		homeworkRepository.deleteById(homeworkId)
		return "Ok"
	}

}