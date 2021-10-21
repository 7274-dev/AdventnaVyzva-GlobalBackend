package com.stsf.globalbackend.models

import javax.persistence.*

@Entity
data class HomeworkSubmissionAttachment (

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @ManyToOne
    val submission: HomeworkSubmission,

    @OneToOne
    val file: File
)