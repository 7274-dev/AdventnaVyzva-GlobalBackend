package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.Homework
import org.springframework.data.jpa.repository.JpaRepository

interface HomeworkRepository : JpaRepository<Homework, Long> {
	fun findAllByClazz(clazz: Class): List<Homework>
	fun findAllByTextContains(s: String): List<Homework>
	fun findAllByTitleContains(s: String): List<Homework>
	fun findAllByTitleContainsOrTextContains(s: String, s1: String = s): List<Homework>
}