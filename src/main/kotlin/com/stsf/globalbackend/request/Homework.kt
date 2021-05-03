package com.stsf.globalbackend.request

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

data class Homework(

	val classId: Long,
	val title: String,
	val text: String,

	@JsonFormat(pattern = "dd-MM-yy HH:mm:ss")
	val due: Date,

	@JsonFormat(pattern = "dd-MM-yy HH:mm:ss")
	val fromDate: Date,

	)