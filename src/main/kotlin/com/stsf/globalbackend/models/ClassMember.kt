package com.stsf.globalbackend.models

import javax.persistence.*

@Entity
data class ClassMember(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @OneToOne
    val user: User,

    @ManyToOne
    val clazz: Class
)
