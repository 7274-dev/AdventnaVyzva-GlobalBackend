package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.Homework
import org.springframework.data.jpa.repository.JpaRepository

interface HomeworkRepository : JpaRepository<Homework, Long> {
	fun findAllByClazz(clazz: Class): List<Homework>
	fun findAllByTextContainsIgnoreCase(s: String): List<Homework>
	fun findAllByTitleContainsIgnoreCase(s: String): List<Homework>
	fun findAllByTitleContainsOrTextContainsIgnoreCase(s: String, s1: String = s): List<Homework>
}