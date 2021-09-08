package com.stsf.globalbackend.models

import com.stsf.globalbackend.filters.EventType
import java.util.*
import javax.persistence.*

@Entity
data class Event(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,

        val eventType: EventType,

        @Temporal(TemporalType.TIMESTAMP)
        val timestamp: Date
)