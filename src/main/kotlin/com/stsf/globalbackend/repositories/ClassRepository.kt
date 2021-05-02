package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.Class
import org.springframework.data.jpa.repository.JpaRepository

interface ClassRepository : JpaRepository<Class, Long>