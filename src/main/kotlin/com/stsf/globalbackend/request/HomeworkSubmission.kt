package com.stsf.globalbackend.request

data class HomeworkSubmission (

    val homeworkId: Long,
    var content: String?,
    val fileIds: List<Long>?

)
