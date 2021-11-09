package com.stsf.globalbackend.models

import javax.persistence.*

@Entity
data class HomeworkSubmission (

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @ManyToOne
    val homework: Homework,

    @ManyToOne
    val user: User,

    @Lob
    val content: String?,

    )