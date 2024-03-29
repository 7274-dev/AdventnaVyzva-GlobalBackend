package com.stsf.globalbackend.services

import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.Homework
import com.stsf.globalbackend.repositories.ClassRepository
import com.stsf.globalbackend.repositories.HomeworkRepository
import com.stsf.globalbackend.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SearchService(
    @Autowired
    private val classRepository: ClassRepository,
    @Autowired
    private val homeworkRepository: HomeworkRepository,
    @Autowired
    private val userRepository: UserRepository
) {
    fun searchHomeworkByTitle(query: String): List<Homework> {
        return homeworkRepository.findAllByTitleContainsIgnoreCase(query)
    }

    fun searchHomeworkByText(query: String): List<Homework> {
        return homeworkRepository.findAllByTextContainsIgnoreCase(query)
    }

    fun searchHomeworkByAnyText(query: String): List<Homework> {
        return homeworkRepository.findAllByTitleContainsOrTextContainsIgnoreCase(query)
    }

    fun searchUserByName(query: String): List<Long> {
        val output: MutableList<Long> = mutableListOf()

        val foundUsers = userRepository.findAllByNameContainsIgnoreCase(query)

        for (user in foundUsers) {
            output.add(user.id)
        }

        return output
    }

    fun searchClassByName(query: String): List<Class> {
        return classRepository.findAllByNameIgnoreCase(query)
    }
}