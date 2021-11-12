package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.NoSuchBallException
import com.stsf.globalbackend.exceptions.NoSuchHomeworkException
import com.stsf.globalbackend.exceptions.NoSuchUserException
import com.stsf.globalbackend.models.Homework
import com.stsf.globalbackend.models.HomeworkBall
import com.stsf.globalbackend.repositories.HomeworkBallRepository
import com.stsf.globalbackend.repositories.HomeworkRepository
import com.stsf.globalbackend.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class HomeworkBallService(
    @Autowired
    private val homeworkBallRepository: HomeworkBallRepository,
    @Autowired
    private val homeworkRepository: HomeworkRepository
) {
    fun createHomeworkBall(homeworkId: Long): HomeworkBall {
        val homework = homeworkRepository.findByIdOrNull(homeworkId) ?: throw NoSuchHomeworkException()

        val ball = HomeworkBall(-1, homework)
        return homeworkBallRepository.save(ball)
    }

    fun deleteHomeworkBall(homeworkBallId: Long) {
        val ball = homeworkBallRepository.findByIdOrNull(homeworkBallId) ?: throw NoSuchHomeworkException()

        homeworkBallRepository.delete(ball)
    }

    fun doesHomeworkHaveBall(homeworkId: Long): Boolean {
        return homeworkBallRepository.findByHomeworkId(homeworkId) != null
    }
}