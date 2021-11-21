package com.stsf.globalbackend.models

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne

data class HomeworkSubmissionFeedback (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,

        @OneToOne
        val homeworkSubmission: HomeworkSubmission,

        @OneToOne
        val feedback: HomeworkSubmissionFeedbackEnum
        )