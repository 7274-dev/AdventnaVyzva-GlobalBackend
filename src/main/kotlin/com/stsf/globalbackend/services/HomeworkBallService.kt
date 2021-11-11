package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.NoSuchBallException
import com.stsf.globalbackend.exceptions.NoSuchHomeworkException
import com.stsf.globalbackend.exceptions.NoSuchUserException
import com.stsf.globalbackend.models.Homework
import com.stsf.globalbackend.models.HomeworkBall
import com.stsf.globalbackend.repositories.HomeworkBallRepository
import com.stsf.globalbackend.repositories.HomeworkRepository
import com.stsf.globalbackend.repositories.UserRepository
import com.stsf.globalbackend.request.GenericResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class HomeworkBallService(
    @Autowired
    private val homeworkBallRepository: HomeworkBallRepository,
    @Autowired
    private val homeworkRepository: HomeworkRepository,
    @Autowired
    private val userRepository: UserRepository,
) {

    fun getAllBallsByUserId(userId: Long): List<HomeworkBall> {
        return homeworkBallRepository.getAllByUserId(userId)
    }

    fun addBall(userId: Long, homeworkId: Long): GenericResponse<SafeHomeworkBall> {
        val homework = homeworkRepository.findByIdOrNull(homeworkId) ?: throw NoSuchHomeworkException()
        val user = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()
        var ball = HomeworkBall(-1, homework, user)

        ball = homeworkBallRepository.save(ball)

        val safeHomeworkBall = SafeHomeworkBall(ball.id, ball.user.id, ball.homework)
        return GenericResponse(safeHomeworkBall)
    }

    fun deleteBallByUserIdAndHomeworkId(userId: Long, homeworkId: Long) {
        val homework = homeworkRepository.findByIdOrNull(homeworkId) ?: throw NoSuchHomeworkException()
        val user = userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()
        val ball = HomeworkBall(-1, homework, user)
        homeworkBallRepository.delete(ball)
    }

    fun deleteBallById(ballId: Long) {
        val ball = homeworkBallRepository.findByIdOrNull(ballId) ?: throw NoSuchBallException()
        homeworkBallRepository.delete(ball)
    }

}

// homework ball without user credentials
data class SafeHomeworkBall(
    val id: Long,
    val userId: Long,
    val homework: Homework
)