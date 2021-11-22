package com.stsf.globalbackend.services

import com.stsf.globalbackend.repositories.HomeworkSubmissionFeedbackRepository
import com.stsf.globalbackend.repositories.HomeworkSubmissionRepository
import com.stsf.globalbackend.request.HomeworkSubmissionFeedback
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HomeworkSubmissionFeedbackService (
    @Autowired
    val submissionRepository: HomeworkSubmissionRepository,
    @Autowired
    val submissionFeedbackRepository: HomeworkSubmissionFeedbackRepository
) {

    fun addFeedbackToSubmission(homeworkSubmissionId: Long, homeworkSubmissionFeedback: HomeworkSubmissionFeedback): HomeworkSubmissionFeedback {

        val submission = submissionRepository.getOne(homeworkSubmissionId)
        val feedback = homeworkSubmissionFeedback.feedback

        val submissionFeedback = com.stsf.globalbackend.models.HomeworkSubmissionFeedback(-1, submission, feedback)

        submissionFeedbackRepository.save(submissionFeedback)

        return homeworkSubmissionFeedback
    }

    fun getFeedbackBySubmissionId(submissionId: Long): com.stsf.globalbackend.models.HomeworkSubmissionFeedback? {
        return submissionFeedbackRepository.findOneByHomeworkSubmissionId(submissionId)
    }

    fun deleteFeedbackFromSubmission(feedbackId: Long) {
        submissionFeedbackRepository.deleteById(feedbackId)
    }
    fun editFeedbackForSubmission(feedbackId: Long) {

    }

}