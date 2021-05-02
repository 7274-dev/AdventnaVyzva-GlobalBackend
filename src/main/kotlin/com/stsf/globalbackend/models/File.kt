package com.stsf.globalbackend.models

import javax.persistence.*

@Entity
data class File(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    val name: String,
    val path: String,
)
// TODO: Add a entity to represent homework to file relation