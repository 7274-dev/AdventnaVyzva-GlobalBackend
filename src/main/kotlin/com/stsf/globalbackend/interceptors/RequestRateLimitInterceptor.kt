package com.stsf.globalbackend.interceptors

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestRateLimitInterceptor : HandlerInterceptor {

	companion object {
		private const val MAX_REQUESTS = 100
		private var cache: Cache<String, Int> = CacheBuilder.newBuilder()
			.expireAfterAccess(Duration.ofSeconds(2))
			.build()

		private fun addRequest(token: String): Int {
			val amountOfRequests = cache.getIfPresent(token)

			return if (amountOfRequests != null) {
				cache.put(token, amountOfRequests + 1)
				amountOfRequests + 1
			} else {
				cache.put(token, 1)
				1
			}
		}

		fun isOverLimit(token: String): Boolean {
			return addRequest(token) >= MAX_REQUESTS
		}
	}

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		val token = request.getHeader("token") ?: return true

		if (isOverLimit(token)) {
			response.sendError(429)
			return false
		}

		return true

	}
}
