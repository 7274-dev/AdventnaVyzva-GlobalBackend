package com.stsf.globalbackend.interceptors

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationInterceptor : HandlerInterceptor {
    companion object {
        private var cache: Cache<String, Long> = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofHours(2))
            .build()

        fun addToken(token: String, userId: Long) {
            cache.put(token, userId)
        }
        
        fun invalidateToken(token: String) {
            cache.invalidate(token)
        }
    }

    // TODO: add token validation
    private fun isValid(token: String): Boolean {
        return cache.getIfPresent(token) != null
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token: String? = request.getHeader("token")

        if (token == null || !isValid(token)) {
            response.sendError(401)
            return false
        }

        return true
    }
}