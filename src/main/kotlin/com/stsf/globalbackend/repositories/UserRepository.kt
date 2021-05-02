package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun getUsersByUsername(username: String): List<User>
}