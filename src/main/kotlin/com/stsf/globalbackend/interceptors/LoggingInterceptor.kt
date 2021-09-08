package com.stsf.globalbackend.interceptors


import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import java.time.Instant
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@WebFilter("/**")
class LoggingInterceptor : HandlerInterceptor, Filter {

	private val logger: Logger = LoggerFactory.getLogger(LoggingInterceptor::class.java)

//
//  This function will be replaced with doFilter as it allows us to log the time each request took to complete
//
//	override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
//
//		val message: String = if (response.status >= 500) {
//			"[${Kolor.foreground("ERROR", Color.RED)}] Status code: ${response.status}"
//		} else {
//			"[${Kolor.foreground("OK", Color.GREEN)}] Status code: ${response.status}"
//		}
//
//		logger.info(message)
//	}

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		// TODO Finish else statement

		 val message: String
		 var token: String? = request.getHeader("token")

		if (token == null) {
			token = "No token"
		}

		message = "[${Kolor.foreground(request.method, Color.CYAN)}] --> ${Kolor.foreground(request.requestURI.toString(), Color.GREEN)} ${request.remoteAddr} token=${Kolor.foreground(token, Color.MAGENTA)}"

		if (request.requestURI.equals("/error")) {
			return true
		}

		logger.info(message)

		return true
	}

	override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

		//val request = request as HttpServletRequest
		val response = response as HttpServletResponse
		val start = Instant.now()
		var message: String

		try {
			chain?.doFilter(request, response)
		} finally {
			val stop = Instant.now()
			val result = Duration.between(start, stop).toMillis()

			message = if (response.status >= 500) {
				"[${Kolor.foreground("ERROR", Color.RED)}] Status code: ${response.status}"
			} else if (response.status in 200..299){
				"[${Kolor.foreground("OK", Color.GREEN)}] Status code: ${response.status} - [${Kolor.foreground("TIME", Color.BLUE)}] $result ms"
			} else {
				"[${Kolor.foreground("WARN", Color.YELLOW)}] Status code: ${response.status}"
			}

		}

		logger.info(message)
	}
}