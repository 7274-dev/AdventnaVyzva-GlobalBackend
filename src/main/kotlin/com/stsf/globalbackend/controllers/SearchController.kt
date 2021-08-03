package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.Homework
import com.stsf.globalbackend.request.GenericResponse
import com.stsf.globalbackend.services.SearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/search")
class SearchController(
    @Autowired
    private val searchService: SearchService
) {

    // We should check for permissions

    @GetMapping("/class")
    fun searchClassesByName(@RequestParam query: String): GenericResponse<List<Class>> {
    return GenericResponse(searchService.searchClassByName(query))
    }

    @GetMapping("/homework/title")
    fun searchHomeworkByTitle(@RequestParam query: String): GenericResponse<List<Homework>> {
        return GenericResponse(searchService.searchHomeworkByTitle(query))
    }

    @GetMapping("/homework/text")
    fun searchHomeworkByText(@RequestParam query: String): GenericResponse<List<Homework>> {
        return GenericResponse(searchService.searchHomeworkByText(query))
    }

    @GetMapping("/homework/any")
    fun searchHomeworkByAnyText(@RequestParam query: String): GenericResponse<List<Homework>> {
        return GenericResponse(searchService.searchHomeworkByAnyText(query))
    }

    @GetMapping("/user")
    fun searchUsersByName(@RequestParam query: String): GenericResponse<List<Long>> {
        return GenericResponse(searchService.searchUserByName(query))
    }
}