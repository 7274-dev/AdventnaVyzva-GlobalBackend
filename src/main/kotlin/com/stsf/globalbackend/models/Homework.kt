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
    var clazz: Class,

    var title: String,
    var text: String,

    @Temporal(TemporalType.DATE)
    var due: Date,

    @Temporal(TemporalType.TIMESTAMP)
    var fromDate: Date
)
