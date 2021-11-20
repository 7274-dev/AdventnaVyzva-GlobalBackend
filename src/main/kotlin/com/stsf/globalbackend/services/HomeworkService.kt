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
	private val homeworkAttachmentRepository: HomeworkAttachmentRepository,
	@Autowired
	private val fileRepository: FileRepository,
	@Autowired
	private val homeworkBallRepository: HomeworkBallRepository
) {

	@Throws(NoSuchHomeworkException::class)
	fun getAttachmentsForHomework(homeworkId: Long): List<HomeworkAttachment> {
		return homeworkAttachmentRepository.getAllByHomework_Id(homeworkId)
	}

	@Throws(NoSuchHomeworkException::class, NoSuchFileException::class)
	fun addAttachmentToHomeworkSubmission(homeworkAttachment: com.stsf.globalbackend.request.HomeworkAttachment): HomeworkAttachment {
		val homework = homeworkRepository.findByIdOrNull(homeworkAttachment.homeworkId) ?: throw NoSuchHomeworkException()
		val file = fileRepository.findByIdOrNull(homeworkAttachment.fileId) ?: throw NoSuchFileException()

		val attachment = HomeworkAttachment(-1, homework, file)

		return homeworkAttachmentRepository.save(attachment)
  	}

	fun getSubmissionsByUser(userId: Long): List<HomeworkSubmission> {
		return homeworkSubmissionRepository.getAllByUser_Id(userId)
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
		val homeworkAttachments = getAttachmentsForHomework(homeworkId)
		val homeworkSubmissions = homeworkSubmissionRepository.getAllByHomework_Id(homeworkId)
		val homeworkSubmissionAttachments: ArrayList<HomeworkSubmissionAttachment> = ArrayList()

		for (submission in homeworkSubmissions) {
			for (attachment in homeworkSubmissionAttachmentRepository.findAllBySubmissionId(submission.id)) {
				java.io.File(attachment.file.path).delete()

				homeworkSubmissionAttachments.add(attachment)
			}
		}

		homeworkSubmissionAttachmentRepository.deleteInBatch(homeworkSubmissionAttachments)
		homeworkSubmissionRepository.deleteInBatch(homeworkSubmissions)
		homeworkAttachmentRepository.deleteInBatch(homeworkAttachments)

		val homeworkBall = homeworkBallRepository.findByHomeworkId(homeworkId)
		if (homeworkBall != null) {
			homeworkBallRepository.delete(homeworkBall)
		}

		homeworkRepository.deleteById(homeworkId)
	}

	fun submitHomework(homeworkSubmission: HomeworkSubmission, attachmentIds: List<Long>?) {
		val attachments = attachmentIds?.stream()?.map { homeworkAttachmentRepository.findByIdOrNull(it) } ?: {}

		println(attachments)
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

