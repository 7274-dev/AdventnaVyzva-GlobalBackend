package com.stsf.globalbackend.request

import com.stsf.globalbackend.models.HomeworkSubmissionFeedbackEnum

data class HomeworkSubmissionFeedback (

        val feedback: HomeworkSubmissionFeedbackEnum,
        val message: String

        )