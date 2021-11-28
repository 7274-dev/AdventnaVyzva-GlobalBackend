package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.HomeworkSubmissionFeedback
import com.stsf.globalbackend.models.HomeworkSubmissionFeedbackEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface HomeworkSubmissionFeedbackRepository : JpaRepository<HomeworkSubmissionFeedback, Long> {
    @Query("from HomeworkSubmissionFeedback s where s.homeworkSubmission.id = :submissionId")
    fun findAllByHomeworkSubmissionId(submissionId: Long): List<HomeworkSubmissionFeedback>

    @Query("select f.feedback as feedback, f.message as message, f.id as id from HomeworkSubmissionFeedback f inner join HomeworkSubmission s on s.homework.id = :homeworkId and s.user.id = :userId where f.homeworkSubmission.id = s.id")
    fun findAllByHomeworkIdAndSubmissionUserId(homeworkId: Long, userId: Long): List<SubmissionFeedbackAndMessage>
}

interface SubmissionFeedbackAndMessage {
    var id: Long
    var feedback: HomeworkSubmissionFeedbackEnum
    var message: String
}