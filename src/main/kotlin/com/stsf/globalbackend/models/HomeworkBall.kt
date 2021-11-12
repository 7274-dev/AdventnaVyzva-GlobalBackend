package com.stsf.globalbackend.models

import javax.persistence.*

@Entity
data class HomeworkBall(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @OneToOne
    val homework: Homework
)