package com.stsf.globalbackend.services

import com.stsf.globalbackend.filters.EventType
import com.stsf.globalbackend.models.Event
import com.stsf.globalbackend.repositories.EventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class EventService(
        @Autowired
        private val eventRepository: EventRepository
) {
    fun produceEvent(eventType: EventType): Event {
        val currentTimestamp = Date.from(Instant.now())

        val event = Event(-1, eventType, currentTimestamp)
        return eventRepository.save(event)
    }
}