package com.stsf.globalbackend.models

import javax.persistence.*

@Entity
data class HomeworkSubmissionFeedback (
        // FIXME: https://discordapp.com/channels/770229888195493888/833685761470627910/912768117405536326
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,

        @OneToOne
        val homeworkSubmission: HomeworkSubmission,

        @Enumerated(EnumType.STRING)
        val feedback: HomeworkSubmissionFeedbackEnum
        )