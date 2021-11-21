package com.stsf.globalbackend.services

import com.stsf.globalbackend.repositories.HomeworkSubmissionFeedbackRepository
import com.stsf.globalbackend.repositories.HomeworkSubmissionRepository
import com.stsf.globalbackend.request.HomeworkSubmissionFeedback
import org.springframework.beans.factory.annotation.Autowired

class HomeworkSubmissionFeedbackService (
    @Autowired
    val submissionRepository: HomeworkSubmissionRepository,
    @Autowired
    val submissionFeedbackRepository: HomeworkSubmissionFeedbackRepository
) {

    fun addFeedbackToSubmission(homeworkSubmissionId: Long, homeworkSubmissionFeedback: HomeworkSubmissionFeedback) {

        val submission = submissionRepository.getOne(homeworkSubmissionId)
        val feedback = homeworkSubmissionFeedback.feedback

        val submissionFeedback = com.stsf.globalbackend.models.HomeworkSubmissionFeedback(-1, submission, feedback)

        submissionFeedbackRepository.save(submissionFeedback)
    }
    fun funDeleteFeedbackFromSubmission(feedbackId: Long) {

    }
    fun editFeedbackForSubmission(feedbackId: Long) {

    }

}