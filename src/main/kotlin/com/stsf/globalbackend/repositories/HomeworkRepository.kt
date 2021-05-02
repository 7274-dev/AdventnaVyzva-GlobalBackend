package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.Homework
import org.springframework.data.jpa.repository.JpaRepository

interface HomeworkRepository : JpaRepository<Homework, Long> {
	fun findAllByClass(clazz: Class): List<Homework>
}