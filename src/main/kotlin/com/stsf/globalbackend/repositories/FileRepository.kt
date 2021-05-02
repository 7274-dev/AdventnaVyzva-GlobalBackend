package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.File
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository : JpaRepository<File, Long>