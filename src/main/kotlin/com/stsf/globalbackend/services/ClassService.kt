package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.NoSuchClassException
import com.stsf.globalbackend.exceptions.NoSuchUserException
import com.stsf.globalbackend.models.ClassMember
import com.stsf.globalbackend.repositories.ClassMemberRepository
import com.stsf.globalbackend.repositories.ClassRepository
import com.stsf.globalbackend.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ClassService (
	@Autowired
	private val userRepository: UserRepository,
	@Autowired
	private val classRepository: ClassRepository,
	@Autowired
	private val classMemberRepository: ClassMemberRepository,
) {

	fun addUserToClass(userId: Long, classId: Long): ClassMember {

		val clazz = classRepository.findByIdOrNull(classId) ?: throw NoSuchClassException()
		val user =  userRepository.findByIdOrNull(userId) ?: throw NoSuchUserException()

		val classMember = ClassMember(-1, user, clazz)
		return classMemberRepository.save(classMember)

	}

	fun removeUserFromClass(userId: Long, classId: Long) {
		val members = classMemberRepository.findAllByClassAndUserId(classId, userId)
		classMemberRepository.deleteInBatch(members)
	}

}