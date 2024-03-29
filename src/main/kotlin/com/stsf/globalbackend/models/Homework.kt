package com.stsf.globalbackend.models

import com.fasterxml.jackson.annotation.JsonFormat
import net.bytebuddy.implementation.bytecode.StackManipulation
import java.util.*
import javax.persistence.*

@Entity
data class Homework(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @ManyToOne
    var clazz: Class,

    @Column(length = 2048)
    var title: String,

    @Column(length = 8192)
    var text: String,

    @Temporal(TemporalType.DATE)
    var due: Date,

    @Temporal(TemporalType.TIMESTAMP)
    var fromDate: Date,
)
