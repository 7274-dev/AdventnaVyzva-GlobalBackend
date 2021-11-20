package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.HomeworkSubmission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface HomeworkSubmissionRepository : JpaRepository<HomeworkSubmission, Long> {
    fun getAllByHomework_Id(homeworkId: Long): List<HomeworkSubmission>
    fun getAllByUser_Id(userId: Long): List<HomeworkSubmission>

    @Query("select HomeworkSubmission as s from HomeworkSubmission where s.id = :userId and s.homework.id = :homeworkId")
    fun getAllByUserAndHomeworkId(@Param("userId") userId: Long, @Param("homeworkId") homeworkId: Long): List<HomeworkSubmission>
}