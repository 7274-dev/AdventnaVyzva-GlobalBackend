package com.stsf.globalbackend.request

import com.stsf.globalbackend.controllers.RawFile
import org.springframework.web.multipart.MultipartFile
import java.io.File

data class File(
    val filename: String,
    val data: MultipartFile
)