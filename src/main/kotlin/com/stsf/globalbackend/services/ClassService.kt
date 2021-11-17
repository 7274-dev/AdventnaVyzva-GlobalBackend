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

	fun getAllUsersNotInClass(classId: Long): List<ClassMember> {
		return classMemberRepository.getAllByClassIdNotEqualTo(classId)
	}

	fun removeUserFromClass(userId: Long, classId: Long) {
		val members = classMemberRepository.findAllByClassAndUserId(classId, userId)
		classMemberRepository.deleteInBatch(members)
	}

	fun getClassById(classId: Long): Class {
		if (!classRepository.existsById(classId)) {
			throw NoSuchClassException()
		}

		return classRepository.getOne(classId)
	}

	fun createClass(className: String): Class {

		// Since we've (ivicek) decided that there can be classes with same name, check is useless
		val newClass = Class(-1, className)
		return classRepository.save(newClass)

	}

	fun deleteClassAndOrphanClassMembers(classId: Long) {
		classMemberRepository.deleteInBatch(classMemberRepository.findAllByClassId(classId))

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

	fun changeClassName(classId: Long, className: String): Class {
		val clazz = classRepository.findByIdOrNull(classId) ?: throw NoSuchClassException()

		return classRepository.save(Class(clazz.id, className))
	}

}
