package com.stsf.globalbackend.models


// TODO: Add annotations when db is configured
data class User(
    val id: Long,
    val username: String,
    val password: String
)