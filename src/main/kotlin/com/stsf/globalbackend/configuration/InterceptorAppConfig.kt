package com.stsf.globalbackend.configuration

import com.stsf.globalbackend.interceptors.AuthenticationInterceptor
import com.stsf.globalbackend.logging.LoggingInterceptor
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class InterceptorAppConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        // Authentication Interceptor
        registry.addInterceptor(AuthenticationInterceptor())
            .addPathPatterns("/api/**") // authenticated paths here
        // Logging Interceptor
        registry.addInterceptor(LoggingInterceptor())
            .addPathPatterns("/api/**")
            // Maybe .excludePatterns("/api/login")?
    }
}