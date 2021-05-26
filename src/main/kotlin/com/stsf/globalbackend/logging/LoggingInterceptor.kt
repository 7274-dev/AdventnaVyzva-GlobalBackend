package com.stsf.globalbackend.logging

import com.stsf.globalbackend.misc.Colors
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoggingInterceptor : HandlerInterceptor {

	val logger: Logger = LoggerFactory.getLogger(LoggingInterceptor::class.java)

	override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
		// Add stuff sometime later
	}

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		// TODO Finish else statement

		 var message: String
		 var token = request.getHeader("token")

		if (token != null) {

			message = "\u001B[32m" + request.remoteAddr + "\u001B[0m" + "[" + "[\u001B[35m" + token + "\u001B[0m]" + "]" + " --> " + "\u001B[36m" + request.method + "\u001B[34m" + request.requestURI + "\u001B[0m"
			logger.info(message)
		}

		return true
	}
}