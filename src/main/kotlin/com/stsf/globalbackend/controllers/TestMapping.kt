package com.stsf.globalbackend.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TestMapping {

    @GetMapping("/test")
    fun test(): String {
        return "It works!"
    }
}