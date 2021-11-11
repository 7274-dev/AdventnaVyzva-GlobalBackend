package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.HomeworkBall
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface HomeworkBallRepository : JpaRepository<HomeworkBall, Long> {

    // Check these functions in ultimate version to see if they work
    // @Query("from HomeworkBall hwb where hwb.user.id = :userId")
    //fun getAllByUserId(@Param("userId") userId: Long): List<HomeworkBall>
    fun getAllByUserId(userId: Long): List<HomeworkBall>

    fun getByHomework_Id(homework_id: Long): HomeworkBall
}