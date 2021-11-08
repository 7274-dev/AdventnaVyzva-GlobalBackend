package com.stsf.globalbackend.services

import com.stsf.globalbackend.configuration.ConfigurationProperties
import com.stsf.globalbackend.exceptions.NoSuchFileException
import com.stsf.globalbackend.repositories.FileRepository
import com.stsf.globalbackend.repositories.HomeworkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.nio.file.Path
import java.nio.file.Paths

@Service
class FileService(
    @Autowired
    private val fileRepository: FileRepository,
    @Autowired
    private val homeworkRepository: HomeworkRepository,
    @Autowired
    private val environment: Environment
) {

    fun getStorageDirectoryPath(): String {
        return environment.getProperty("stsf.config.storageDirectory") ?: ConfigurationProperties().storageDirectory
    }

    fun appendToPath(path: String, filename: String): String {
        return if (!path.endsWith("/")) {
            "$path/$filename"
        } else {
            "$path$filename"
        }
    }

    fun getFile(fileId: Long): File {
        val path = (fileRepository.findByIdOrNull(fileId) ?: throw NoSuchFileException()).path

        return File(path)
    }


    fun uploadFile(file: MultipartFile): com.stsf.globalbackend.models.File {
        val filename = file.originalFilename ?: file.name

        if (File(filename).exists()) {
            throw FileAlreadyExistsException(File(filename))
        }

        if (!File(getStorageDirectoryPath()).exists()) {
            File(getStorageDirectoryPath()).mkdir()
        }

        val newFilePath = appendToPath(getStorageDirectoryPath(), filename)

        File(newFilePath).createNewFile()

        file.transferTo(Paths.get(newFilePath))

        return fileRepository.save(com.stsf.globalbackend.models.File(-1, filename, newFilePath))
    }
}