package com.stsf.globalbackend.configuration

import com.stsf.globalbackend.interceptors.AuthenticationInterceptor
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class InterceptorAppConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AuthenticationInterceptor())
            .addPathPatterns("/api/**") // authenticated paths here
    }
}