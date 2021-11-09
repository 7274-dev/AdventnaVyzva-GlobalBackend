package com.stsf.globalbackend.request

import org.springframework.web.multipart.MultipartFile

data class File(
    val filename: String,
    val data: MultipartFile
)