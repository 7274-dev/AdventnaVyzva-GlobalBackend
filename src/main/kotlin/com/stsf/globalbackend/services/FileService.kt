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
import java.util.*

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

    fun getFileName(fileId: Long): String {
        return (fileRepository.findByIdOrNull(fileId) ?: throw NoSuchFileException()).name
    }

    fun getFile(fileId: Long): File {
        val path = (fileRepository.findByIdOrNull(fileId) ?: throw NoSuchFileException()).path

        return File(path)
    }

    fun getFileExtension(filename: String): String {
        if (filename.contains(".")) {
            val extensions = filename.split(".")

            if (extensions.size == 2) {
                return "." + extensions.last()
            }

            var extension = ""
            for (ext in extensions) {
                if (extensions.indexOf(ext) == 0) {
                    continue
                }

                extension += ".$ext"
            }

            return extension
        } else {
            return ""
        }
    }

    fun findNextFreeFilename(path: String, filename: String): String {
        val fileExtension = getFileExtension(filename)
        val filenameWithoutExtension = filename.replace(fileExtension, "")

        var newFilename = filename
        var counter = 1

        var filePath = appendToPath(path, newFilename)
        while (File(filePath).exists()) {
            newFilename = "${filenameWithoutExtension}-${counter}${fileExtension}"

            filePath = appendToPath(path, newFilename)
            counter++
        }

        return newFilename
    }


    fun uploadFile(file: MultipartFile): com.stsf.globalbackend.models.File {
        val filename = file.originalFilename ?: file.name

        if (!File(getStorageDirectoryPath()).exists()) {
            File(getStorageDirectoryPath()).mkdir()
        }

        var newFilePath = appendToPath(getStorageDirectoryPath(), filename)

        if (File(newFilePath).exists()) {
           newFilePath = appendToPath(getStorageDirectoryPath(), findNextFreeFilename(getStorageDirectoryPath(), filename))
        }

        File(newFilePath).createNewFile()

        file.transferTo(Paths.get(newFilePath))

        return fileRepository.save(com.stsf.globalbackend.models.File(-1, filename, newFilePath))
    }
}