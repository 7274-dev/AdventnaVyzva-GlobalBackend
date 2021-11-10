package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.models.HomeworkBall
import com.stsf.globalbackend.repositories.HomeworkBallRepository
import com.stsf.globalbackend.repositories.HomeworkRepository
import com.stsf.globalbackend.request.GenericResponse
import com.stsf.globalbackend.services.AuthenticationService
import com.stsf.globalbackend.services.HomeworkBallService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


// Maybe work on the naming
@RestController
@RequestMapping("/api/homework/balls")
class HomeworkBallController (
    @Autowired
    private val auth: AuthenticationService,
    @Autowired
    private val ballService: HomeworkBallService,
    ) {

    @PutMapping("")
    fun addBall(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<HomeworkBall> {
        val authenticatedUser = auth.getUserByToken(token)
        return GenericResponse(ballService.addBall(authenticatedUser.id, homeworkId))

    }

    @DeleteMapping("")
    fun deleteBall(@RequestHeader token: String, @RequestParam ballId: Long) {
        val authenticatedUser = auth.getUserByToken(token)
        val balls = ballService.getAllBallsByUserId(authenticatedUser.id)

        for (ball in balls) {
            if (ball.id == ballId) {
                ballService.deleteBallById(ball.id)
            }
        }

    }

    @DeleteMapping("/homework")
    fun deleteBallByHomeworkId(@RequestHeader token: String, @RequestParam homeworkId: Long) {
        val authenticatedUser = auth.getUserByToken(token)
        val balls = ballService.getAllBallsByUserId(authenticatedUser.id)

        for (ball in balls) {
            if (ball.homework.id == homeworkId) {
                ballService.deleteBallByUserIdAndHomeworkId(authenticatedUser.id, homeworkId)
            }
        }
    }

    @GetMapping("")
    fun getAllBalls(@RequestHeader token: String): GenericResponse<List<HomeworkBall>> {
        val authenticatedUser = auth.getUserByToken(token)
        return GenericResponse(ballService.getAllBallsByUserId(authenticatedUser.id))
    }

}