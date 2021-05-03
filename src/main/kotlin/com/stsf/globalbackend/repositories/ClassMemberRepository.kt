package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.ClassMember
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ClassMemberRepository : JpaRepository<ClassMember, Long> {
	@EntityGraph(value = "User.id")
	fun findByUserId(id: Long): List<ClassMember>

	@Query("from ClassMember c where c.clazz.id = :id and c.user.id = :u_id")
	fun findAllByClassAndUserId(@Param("id") classId: Long, @Param("u_id") userId: Long): List<ClassMember>
}