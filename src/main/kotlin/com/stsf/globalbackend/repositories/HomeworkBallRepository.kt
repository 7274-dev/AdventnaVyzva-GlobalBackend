package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.Homework
import com.stsf.globalbackend.models.HomeworkBall
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface HomeworkBallRepository : JpaRepository<HomeworkBall, Long> {
    fun findByHomeworkId(homeworkId: Long): HomeworkBall?
}