package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.ClassAlreadyExistsException
import com.stsf.globalbackend.exceptions.NoSuchClassException
import com.stsf.globalbackend.exceptions.NoSuchUserException
import com.stsf.globalbackend.models.Class
import com.stsf.globalbackend.models.ClassMember
import com.stsf.globalbackend.models.User
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
		// Thanks ivicek <3
		val members = classMemberRepository.findAllByClassAndUserId(classId, userId)
		classMemberRepository.deleteInBatch(members)
	}

	fun createClass(className: String): Class {

		// Since we've (ivicek) decided that there can be classes with same name, check is useless
		var newClass = Class(-1, className)
		return classRepository.save(newClass)

	}

	fun deleteClass(classId: Long) {

		// Maybe do some black magic stuff and remove ClassMember?
		classRepository.deleteById(classId)

	}


	fun getAllClasses(): List<Class> {
		return classRepository.findAll()
	}


	// Maybe change return to UserID
	fun getAllUsersInClass(classId: Long): List<User> {
		val classMembers = classMemberRepository.findAllByClassId(classId)
		val users: ArrayList<User> = ArrayList()

		for (classMember in classMembers) {
			if (!classMember.user.isTeacher && !classMember.user.isAdmin) {
				users.add(classMember.user)
			}
		}
		return users
	}

}
