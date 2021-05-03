package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.Class
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ClassRepository : JpaRepository<Class, Long> {
	fun findAllByName(name: String): List<Class>
	fun findClassById(id: Long): Class
	fun findAllByNameContains(s: String): List<Class>



}