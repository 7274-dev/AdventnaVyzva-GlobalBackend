package com.stsf.globalbackend.exceptionhandlers

import com.stsf.globalbackend.exceptions.*
import com.stsf.globalbackend.request.GenericResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.io.NoSuchFileException

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(e: BadCredentialsException): ResponseEntity<GenericResponse<String>> {
        return ResponseEntity(GenericResponse("Bad credentials"), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(BadTokenException::class)
    fun handleBadTokenException(e: BadTokenException): ResponseEntity<GenericResponse<String>> {
        return ResponseEntity(GenericResponse("Bad token"), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ClassAlreadyExistsException::class)
    fun handleClassAlreadyExistsException(e: ClassAlreadyExistsException): ResponseEntity<GenericResponse<String>> {
        return ResponseEntity(GenericResponse("Class already exists"), HttpStatus.CONFLICT)
    }

    @ExceptionHandler(InsufficientPermissionsException::class)
    fun handleInsufficientPermissionsException(e: InsufficientPermissionsException): ResponseEntity<GenericResponse<String>> {
        return ResponseEntity(GenericResponse("Insufficient permissions"), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(NoSuchException::class)
    fun handleNoSuchExceptions(e: NoSuchException): ResponseEntity<GenericResponse<String>> {
        return ResponseEntity(GenericResponse(e.message ?: "No such thing"), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(FileAlreadyExistsException::class)
    fun handleFileAlreadyExistsException(e: FileAlreadyExistsException): ResponseEntity<GenericResponse<String>> {
        return ResponseEntity(GenericResponse("File already exists"), HttpStatus.CONFLICT)
    }
}