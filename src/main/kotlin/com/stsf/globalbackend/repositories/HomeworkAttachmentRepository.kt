package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.HomeworkAttachment
import org.springframework.data.jpa.repository.JpaRepository


interface HomeworkAttachmentRepository : JpaRepository<HomeworkAttachment, Long> {
    fun getAllByHomework_Id(homework_id: Long): List<HomeworkAttachment>
}