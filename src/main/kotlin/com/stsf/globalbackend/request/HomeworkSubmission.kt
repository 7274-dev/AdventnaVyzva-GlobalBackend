package com.stsf.globalbackend.request

import com.stsf.globalbackend.models.HomeworkSubmissionAttachment

data class HomeworkSubmission (
    val id: Long?,
    val user: UserIdAndName?,
    val attachments: List<HomeworkSubmissionAttachment>?,
    val homeworkId: Long,
    var content: String,
    val fileIds: List<Long>
)
