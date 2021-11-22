package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.HomeworkSubmissionFeedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface HomeworkSubmissionFeedbackRepository : JpaRepository<HomeworkSubmissionFeedback, Long> {
    @Query("from HomeworkSubmissionFeedback s where s.homeworkSubmission.id = :submissionId")
    fun findOneByHomeworkSubmissionId(submissionId: Long): HomeworkSubmissionFeedback?
}