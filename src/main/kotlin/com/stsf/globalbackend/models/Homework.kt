package com.stsf.globalbackend.models

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*
import javax.persistence.*

@Entity
data class Homework(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @ManyToOne
    val clazz: Class,

    val title: String,
    val text: String,

    @Temporal(TemporalType.DATE)
    val due: Date,

    @Temporal(TemporalType.TIMESTAMP)
    val fromDate: Date
)
