package com.stsf.globalbackend.interceptors


import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoggingInterceptor : HandlerInterceptor {

	private val logger: Logger = LoggerFactory.getLogger(LoggingInterceptor::class.java)

	override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {

		val message: String = if (response.status >= 500) {
			"[${Kolor.foreground("ERROR", Color.RED)}] Status code: ${response.status}"
		} else {
			"[${Kolor.foreground("OK", Color.GREEN)}] Status code: ${response.status}"
		}

		logger.info(message)
	}

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
}