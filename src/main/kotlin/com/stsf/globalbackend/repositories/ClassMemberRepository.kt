package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.ClassMember
import org.springframework.data.jpa.repository.JpaRepository

interface ClassMemberRepository : JpaRepository<ClassMember, Long>