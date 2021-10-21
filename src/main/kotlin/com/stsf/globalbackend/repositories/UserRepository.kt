package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findAllByUsernameIgnoreCase(username: String): List<User>
    fun findAllByNameContainsIgnoreCase(s: String): List<User>
}