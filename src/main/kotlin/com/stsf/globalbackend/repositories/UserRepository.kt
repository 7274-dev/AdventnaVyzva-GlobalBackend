package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findAllByUsername(username: String): List<User>
    fun findAllByNameContains(s: String): List<User>
}