package com.stsf.globalbackend.request

import com.fasterxml.jackson.annotation.JsonFormat

data class Date (
	@JsonFormat(pattern = "dd-MM-yy HH:mm:ss")
	val date: Date
		)