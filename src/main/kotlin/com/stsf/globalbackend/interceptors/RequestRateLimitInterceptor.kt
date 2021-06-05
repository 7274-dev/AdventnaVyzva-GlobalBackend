package com.stsf.globalbackend.interceptors

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestRateLimitInterceptor : HandlerInterceptor {

	companion object {
		private val MAX_REQUESTS = 500
		private var cache: Cache<String, Int> = CacheBuilder.newBuilder()
			.expireAfterWrite(Duration.ofHours(2))
			.build()

		private fun addRequest(token: String) {
			val amountOfRequests = cache.getIfPresent(token)
			// This can be simplified with ?:
			if (amountOfRequests is Int) {
				cache.put(token, amountOfRequests + 1)
			} else {
				cache.put(token, 1)
			}

		}

		fun getLimit(token: String): Boolean {
			// Ivicek will this work?
			val amountOfRequests: Int = (cache.getIfPresent(token) ?: addRequest(token)) as Int
			return amountOfRequests <= MAX_REQUESTS

		}
	}

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
												 // Do we need to return false if we send 401?
		val token = (request.getHeader("token") ?: response.sendError(401)) as String

		return getLimit(token)

	}
}
