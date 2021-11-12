package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.HomeworkSubmission
import org.springframework.data.jpa.repository.JpaRepository

interface HomeworkSubmissionRepository : JpaRepository<HomeworkSubmission, Long> {
    fun getAllByHomework_Id(homeworkId: Long): List<HomeworkSubmission>
    fun getAllByHomework_User_Id(userId: Long): List<HomeworkSubmission>
}