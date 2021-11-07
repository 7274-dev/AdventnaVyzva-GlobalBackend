package com.stsf.globalbackend.configuration

import com.stsf.globalbackend.interceptors.AuthenticationInterceptor
import com.stsf.globalbackend.interceptors.LoggingInterceptor
import com.stsf.globalbackend.interceptors.RequestRateLimitInterceptor
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class InterceptorAppConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        // Authentication Interceptor
        registry.addInterceptor(AuthenticationInterceptor())
            .addPathPatterns("/api/**") // authenticated paths here
            .excludePathPatterns("/api/admin/create")
            .excludePathPatterns("/api/file/**")
            
        registry.addInterceptor(RequestRateLimitInterceptor())
            .addPathPatterns("/**")

        // Logging Interceptor
        registry.addInterceptor(LoggingInterceptor())
            .addPathPatterns("/**")
    }
}