package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.services.ClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/api/class")
class ClazzController (@Autowired
                       private val classService: ClassService) {

    @GetMapping("/lol")
	fun testClass(@RequestParam className: String) {
	    classService.createClass(className)
	}

}