package com.stsf.globalbackend.request

import com.stsf.globalbackend.models.HomeworkSubmissionFeedbackEnum

data class HomeworkSubmissionFeedback (

        val homeworkSubmission: HomeworkSubmission,
        val feedback: HomeworkSubmissionFeedbackEnum,
        val message: String

        )