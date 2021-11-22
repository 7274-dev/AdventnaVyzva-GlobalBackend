package com.stsf.globalbackend.models

import javax.persistence.*

@Entity
data class HomeworkSubmissionFeedback (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,

        @OneToOne
        val homeworkSubmission: HomeworkSubmission,

        @Enumerated(EnumType.STRING)
        val feedback: HomeworkSubmissionFeedbackEnum
        )