package com.stsf.globalbackend.repositories

import com.stsf.globalbackend.models.Event
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository : JpaRepository<Event, Long>