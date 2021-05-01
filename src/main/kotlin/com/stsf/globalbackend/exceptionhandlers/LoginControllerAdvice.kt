package com.stsf.globalbackend.exceptionhandlers

import com.stsf.globalbackend.exceptions.BadCredentialsException
import com.stsf.globalbackend.request.GenericResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class LoginControllerAdvice {
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(e: BadCredentialsException): ResponseEntity<GenericResponse<String>> {
        return ResponseEntity(GenericResponse("Bad credentials"), HttpStatus.UNAUTHORIZED)
    }
}