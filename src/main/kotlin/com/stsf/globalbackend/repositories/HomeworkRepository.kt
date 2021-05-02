package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.Homework
import org.springframework.data.jpa.repository.JpaRepository

interface HomeworkRepository : JpaRepository<Homework, Long>