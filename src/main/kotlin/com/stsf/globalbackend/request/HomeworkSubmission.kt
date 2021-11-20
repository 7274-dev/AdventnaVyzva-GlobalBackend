package com.stsf.globalbackend.request

data class HomeworkSubmission (
    val id: Long,
    val user: UserIdAndName,
    val homeworkId: Long,
    var content: String?,
    val fileIds: List<Long>
)
