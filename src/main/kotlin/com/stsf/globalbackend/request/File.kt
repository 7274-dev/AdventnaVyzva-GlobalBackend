package com.stsf.globalbackend.request

data class File(
    val filename: String,
    val data: ByteArray
)