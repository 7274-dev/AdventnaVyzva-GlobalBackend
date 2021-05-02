package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.ClassMember
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ClassMemberRepository : JpaRepository<ClassMember, Long> {
	@EntityGraph(value = "User.id")
	fun findByUserId(id: Long): List<ClassMember>
}