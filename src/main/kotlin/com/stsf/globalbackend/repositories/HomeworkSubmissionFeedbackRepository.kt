package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.HomeworkSubmissionFeedback
import org.springframework.data.jpa.repository.JpaRepository

interface HomeworkSubmissionFeedbackRepository : JpaRepository<HomeworkSubmissionFeedback, Long> {

}