@file:Suppress("SpellCheckingInspection")

package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.NoSuchClassException
import com.stsf.globalbackend.exceptions.NoSuchFileException
import com.stsf.globalbackend.models.*
import com.stsf.globalbackend.repositories.*
import com.stsf.globalbackend.exceptions.NoSuchHomeworkException
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
	private val classMemberRepository: ClassMemberRepository,
	@Autowired
	private val homeworkSubmissionRepository: HomeworkSubmissionRepository,
	@Autowired
	private val homeworkSubmissionAttachmentRepository: HomeworkSubmissionAttachmentRepository,
	@Autowired
	private val fileRepository: FileRepository,
) {

	@Throws(NoSuchHomeworkException::class)
	fun getAttachmentsForHomework(homeworkId: Long): List<HomeworkAttachment> {
		return homeworkAttachmentRepository.getAllByHomework_Id(homeworkId)
	}

	@Throws(NoSuchHomeworkException::class)
	fun addAttachmentToHomeworkSubmission(homeworkAttachment: com.stsf.globalbackend.request.HomeworkAttachment): HomeworkAttachment {
		val homework = homeworkRepository.findByIdOrNull(homeworkAttachment.homeworkId) ?: throw NoSuchHomeworkException()

		val attachment = HomeworkAttachment(-1, homework)
		return homeworkAttachmentRepository.save(attachment)
  }


	fun getHomeworkById(homeworkId: Long): Homework {
		if (!homeworkRepository.existsById(homeworkId)) {
			throw NoSuchHomeworkException()
		}

		return homeworkRepository.getOne(homeworkId)
	}

	// No Mapping!
	fun getAllHomeworksByStudent(studentId: Long): List<Homework> {
		val classes: List<ClassMember> = classMemberRepository.findByUserId(studentId)
		val homeworks: ArrayList<Homework> = ArrayList()


		for (clazz in classes) {
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

	fun replaceHomework(homework: Homework): Homework {
		return homeworkRepository.save(homework)
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

	fun submitHomework(homeworkSubmission: HomeworkSubmission, attachmentIds: List<Long>?) {

		if (attachmentIds != null) {
			for (attachment in attachmentIds) {
				val attachmentId = fileRepository.findByIdOrNull(attachment) ?: throw NoSuchFileException()
				val attachment = HomeworkSubmissionAttachment(-1, homeworkSubmission, attachmentId)

				homeworkSubmissionAttachmentRepository.save(attachment)
			}
		}
		homeworkSubmissionRepository.save(homeworkSubmission)
	}

	fun getSubmissions(homeworkId: Long, userId: Long): List<HomeworkSubmission> {
		val output = mutableListOf<HomeworkSubmission>()
		val submissions = homeworkSubmissionRepository.findAll()

		for (submission in submissions) {

			if (submission.user.id == userId) {
				output.add(submission)
			}

		}

		return output
	}

	fun getHomeworkData(homeworkId: Long): Homework {
		return homeworkRepository.findByIdOrNull(homeworkId) ?: throw NoSuchHomeworkException()
	}
}

