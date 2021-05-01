package com.stsf.globalbackend.request

data class GenericResponse<T>(
    val response: T
)