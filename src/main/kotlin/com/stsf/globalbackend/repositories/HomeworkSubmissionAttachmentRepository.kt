package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.HomeworkSubmissionAttachment
import org.springframework.data.jpa.repository.JpaRepository

interface HomeworkSubmissionAttachmentRepository : JpaRepository<HomeworkSubmissionAttachment, Long> {
    fun findAllBySubmissionId(submissionId: Long): List<HomeworkSubmissionAttachment>
}