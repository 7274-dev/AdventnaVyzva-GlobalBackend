package com.stsf.globalbackend.logging


import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
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

		message = Kolor.foreground(request.requestURL.toString(), Color.GREEN) + " [" + Kolor.foreground(token, Color.MAGENTA) + "]  -->  " + Kolor.foreground(request.method, Color.CYAN) + " " + Kolor.foreground(request.method, Color.YELLOW)

		if (request.requestURI.equals("/error")) {
			message = Kolor.background("ERROR", Color.RED) + ", unable to process request"
		}

		logger.info(message)

		return true
	}
}