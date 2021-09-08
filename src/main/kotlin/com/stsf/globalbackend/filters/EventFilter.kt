package com.stsf.globalbackend.filters

import com.stsf.globalbackend.services.EventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.Exception
import javax.servlet.annotation.WebFilter
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

enum class EventType {
    HOMEWORK_CREATE, HOMEWORK_SUBMIT, HOMEWORK_EDIT, HOMEWORK_DELETE, UNKNOWN
}

@Component
@WebFilter("/api/homework")
class HomeworkEventFilter(
        @Autowired
        private val eventService: EventService
) : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val request = request as HttpServletRequest
        val response = response as HttpServletResponse

        val eventType = when(request.requestURI.removeSuffix("/")) {
            "/api/homework" ->
                    when(request.method) {
                        "PATCH" -> EventType.HOMEWORK_EDIT
                        "DELETE" -> EventType.HOMEWORK_DELETE
                        "PUT" -> EventType.HOMEWORK_CREATE
                        else -> EventType.UNKNOWN
                    }

            else -> EventType.UNKNOWN
        }

        try {
            chain?.doFilter(request, response)
        }
        finally {
            if (response.status in 200..299 && eventType != EventType.UNKNOWN) {
                eventService.produceEvent(eventType)
            }
        }
    }

}