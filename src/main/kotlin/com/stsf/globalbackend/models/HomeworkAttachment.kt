package com.stsf.globalbackend.models

import java.util.*
import javax.persistence.*

@Entity
data class HomeworkAttachment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @ManyToOne
    val homework: Homework,

    @OneToOne
    val file: File
    // Dis fine?
)