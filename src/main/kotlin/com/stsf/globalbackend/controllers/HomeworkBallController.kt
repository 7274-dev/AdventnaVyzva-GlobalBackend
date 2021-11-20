package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.exceptions.InsufficientPermissionsException
import com.stsf.globalbackend.models.HomeworkBall
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
    private val authenticationService: AuthenticationService,
    @Autowired
    private val ballService: HomeworkBallService,
) {
    @PutMapping("")
    fun createBall(@RequestHeader token: String, @RequestParam homeworkId: Long): GenericResponse<HomeworkBall> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isTeacher && !authenticatedUser.isAdmin) {
            throw InsufficientPermissionsException()
        }

        return GenericResponse(ballService.createHomeworkBall(homeworkId))
    }

    @DeleteMapping("")
    fun deleteBall(@RequestHeader token: String, @RequestParam ballId: Long): GenericResponse<String> {
        val authenticatedUser = authenticationService.getUserByToken(token)

        if (!authenticatedUser.isTeacher && !authenticatedUser.isAdmin) {
            throw InsufficientPermissionsException()
        }

        ballService.deleteHomeworkBall(ballId)
        return GenericResponse("Ok")
    }

    @GetMapping("")
    fun doesHomeworkHaveBall(@RequestParam homeworkId: Long): GenericResponse<Boolean> {
        return GenericResponse(ballService.doesHomeworkHaveBall(homeworkId))
    }
}