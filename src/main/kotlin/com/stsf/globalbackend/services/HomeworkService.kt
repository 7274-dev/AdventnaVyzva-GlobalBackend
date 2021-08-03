package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.NoSuchClassException
import com.stsf.globalbackend.exceptions.NoSuchHomeworkException
import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.ClassMember
import com.stsf.globalbackend.models.Homework
import com.stsf.globalbackend.models.User
import com.stsf.globalbackend.repositories.ClassMemberRepository
import com.stsf.globalbackend.repositories.ClassRepository
import com.stsf.globalbackend.repositories.HomeworkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

@Service
class HomeworkService (
	@Autowired
	private val homeworkRepository: HomeworkRepository,
	@Autowired
	private val classRepository: ClassRepository,
	@Autowired
	private val classMemberRepository: ClassMemberRepository
) {

	// No Mapping!
	fun getAllHomeworksByStudent(studentId: Long): List<Homework> {
		val clases: List<ClassMember> = classMemberRepository.findByUserId(studentId)
		val homeworks: ArrayList<Homework> = ArrayList()


		for (clazz in clases) {
			homeworks.addAll(homeworkRepository.findAllByClazz(clazz.clazz))
		}

		return homeworks
	}

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

	fun getHomeworkData(homeworkId: Long): Homework {
		return homeworkRepository.findByIdOrNull(homeworkId) ?: throw NoSuchHomeworkException()
	}
}
