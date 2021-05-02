package com.stsf.globalbackend.request

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

data class DateAndClassId(
    @JsonFormat(pattern = "dd-MM-yy HH:mm:ss")
    val date: Date,
    val classId: Long
)