package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<User, Long> {
    fun findAllByUsernameIgnoreCase(username: String): List<User>
    fun findAllByNameContainsIgnoreCase(s: String): List<User>

    @Query("select u.id from User u where not exists(select id from ClassMember where user.id = u.id)")
    fun findAllUsersNotInClass(): List<Long>
}