package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.services.ClassService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/api/class")
class ClazzController (@Autowired classService: ClassService) {

	//TODO Finish this crap, i cant do it because IntelliJ's having a stroke and aneurysm rn
	// createClass, deleteClass, addUserToClass, removeUserFromClass -- classService 

}
