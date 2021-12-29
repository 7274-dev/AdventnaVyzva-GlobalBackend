package com.stsf.globalbackend.controllers


import com.fasterxml.jackson.annotation.JsonFormat
import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.exceptions.NoSuchSubmissionException
import com.stsf.globalbackend.repositories.SubmissionFeedbackAndMessage
import com.stsf.globalbackend.request.*
import com.stsf.globalbackend.services.*
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
	private val classService: ClassService,
	@Autowired
	private val feedbackService: HomeworkSubmissionFeedbackService,
) {
  
	// both attachment mappings have a problem: there is no check about homework ownership!!!
	@GetMapping("/attachment")
	fun getAttachments(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<List<com.stsf.globalbackend.models.HomeworkAttachment>> {
		return GenericResponse(homeworkService.getAttachmentsForHomework(homeworkId))
	}

	@PostMapping("/attachment")
	fun addAttachmentToHomework(@RequestHeader token: String, @RequestBody attachment: HomeworkAttachment): GenericResponse<com.stsf.globalbackend.models.HomeworkAttachment> {
		return GenericResponse(homeworkService.addAttachmentToHomeworkSubmission(attachment))
	}
  
	@PostMapping("/mdtohtml")
	fun getHtmlFromMarkdown(@RequestBody markdown: String): GenericResponse<String> {
		return GenericResponse(markdownService.markdownToHTML(markdownService.htmlEncode(markdown)))
	}

	@GetMapping("")
	fun getHomework(@RequestParam homeworkId: Long): GenericResponse<com.stsf.globalbackend.models.Homework> {
		return GenericResponse(homeworkService.getHomeworkData(homeworkId))
	}

	@PutMapping("")
	fun addHomework(@RequestHeader token: String, @RequestBody homework: Homework): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isTeacher && !authenticatedUser.isAdmin) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(homeworkService.createHomework(homework))
	}

	@DeleteMapping("")
	fun deleteHomework(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<String> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isAdmin && !authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}
		// TODO: Check if teacher owns the class that the homework is assigned to
		homeworkService.deleteHomework(homeworkId)

		return GenericResponse("Ok")
	}

	// Should be fixed? (I didn't test it)
	// - Ivan
	@PatchMapping("")
	fun editHomework(@RequestHeader token: String, @RequestParam homeworkId: Long, @RequestBody homework: Homework): GenericResponse<com.stsf.globalbackend.models.Homework> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isTeacher && !authenticatedUser.isAdmin) {
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
	@PostMapping("/submissions")
	fun submitHomework(@RequestHeader token: String, @RequestBody homeworkSubmission: HomeworkSubmission): GenericResponse<String> {
		return GenericResponse("Homework submission off!")
	}

	@GetMapping("/submissions/user")
	fun getSubmittedHomeworkByUser(@RequestHeader token: String, @RequestParam userId: Long): GenericResponse<List<SafeHomeworkSubmission>> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isTeacher && !authenticatedUser.isAdmin) {
			throw InsufficientPermissionsException()
		}

		val submissions = homeworkService.getSubmissionsByUser(userId)
		val safeSubmissions: MutableList<SafeHomeworkSubmission> = mutableListOf()

		for (submission in submissions) {
			val safeSubmission = SafeHomeworkSubmission(submission.id, submission.user.id, submission.content, submission.homework)
			safeSubmissions.add(safeSubmission)
		}

		return GenericResponse(safeSubmissions)
	}

	@GetMapping("/submissions")
	fun getSubmittedHomework(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<List<HomeworkSubmission>> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isAdmin && !authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(homeworkService.getSubmissions(homeworkId))
	}

	@GetMapping("/done")
	fun isHomeworkDone(@RequestParam homeworkId: Long): GenericResponse<Boolean> {
		return GenericResponse(homeworkService.getSubmissions(homeworkId).isNotEmpty())
	}

	@PutMapping("/feedback")
	fun addFeedbackToSubmission(@RequestHeader token: String, @RequestBody feedback: HomeworkSubmissionFeedback, @RequestParam submissionId: Long): GenericResponse<HomeworkSubmissionFeedback> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isAdmin && !authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		return GenericResponse(feedbackService.addFeedbackToSubmission(submissionId, feedback))
	}

	@GetMapping("/feedback")
	fun getFeedbackForAuthenticatedUser(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<List<SubmissionFeedbackAndMessage>> {
		val authenticatedUser = auth.getUserByToken(token)

//		val submission = homeworkService.getSubmissionsByUser(authenticatedUser.id).find { it.homework.id == homeworkId } ?: throw NoSuchSubmissionException()


		return GenericResponse(feedbackService.getFeedbackByHomeworkAndUserId(authenticatedUser.id, homeworkId))
	}

	@GetMapping("/feedback/submission")
	fun getFeedbackForSubmission(@RequestParam userId: Long, @RequestParam homeworkId: Long): GenericResponse<List<SubmissionFeedbackAndMessage>> {
		return GenericResponse(feedbackService.getFeedbackByHomeworkAndUserId(userId, homeworkId))
	}

	@DeleteMapping("/feedback")
	fun deleteFeedbackById(@RequestHeader token: String, feedbackId: Long): GenericResponse<String> {
		val authenticatedUser = auth.getUserByToken(token)

		if (!authenticatedUser.isAdmin && !authenticatedUser.isTeacher) {
			throw InsufficientPermissionsException()
		}

		feedbackService.deleteFeedbackFromSubmission(feedbackId)

		return GenericResponse("Ok")
	}
}

data class SafeHomeworkSubmission(
	val id: Long,
	val userId: Long,
	val content: String?,
	val homework: com.stsf.globalbackend.models.Homework
)
