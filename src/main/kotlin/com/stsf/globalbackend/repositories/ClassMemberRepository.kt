package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.ClassMember
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ClassMemberRepository : JpaRepository<ClassMember, Long> {
	@Query(value = "from ClassMember  c where c.user.id = :id")
	fun findByUserId(@Param("id") id: Long): List<ClassMember>

	@Query("from ClassMember c where c.clazz.id = :id and c.user.id = :u_id")
	fun findAllByClassAndUserId(@Param("id") classId: Long, @Param("u_id") userId: Long): List<ClassMember>

	@Query("from ClassMember c where c.clazz.id = :id")
	fun findAllByClassId(@Param("id") classId: Long): List<ClassMember>

	@Query("from ClassMember c where c.clazz.id <> :id")
	fun getAllByClassIdNotEqualTo(@Param("id") classId: Long): List<ClassMember>
}