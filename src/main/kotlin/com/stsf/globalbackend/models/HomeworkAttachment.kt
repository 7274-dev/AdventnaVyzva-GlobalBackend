package com.stsf.globalbackend.models

import javax.persistence.*

@Entity
data class HomeworkAttachment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @ManyToOne
    val homework: Homework
)