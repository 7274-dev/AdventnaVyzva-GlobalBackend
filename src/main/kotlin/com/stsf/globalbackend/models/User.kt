package com.stsf.globalbackend.models

import javax.persistence.*

@Entity
@Table(name="user_table") // the user table name is used by postgres
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    var username: String,
    var password: String,
    var name: String,
    var isTeacher: Boolean
)