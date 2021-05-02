package com.stsf.globalbackend.services

import com.stsf.globalbackend.exceptions.NoSuchFileException
import com.stsf.globalbackend.exceptions.NoSuchHomeworkException
import com.stsf.globalbackend.repositories.FileRepository
import com.stsf.globalbackend.repositories.HomeworkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.*
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@Service
class FileService(
    @Autowired
    private val fileRepository: FileRepository,
    @Autowired
    private val homeworkRepository: HomeworkRepository
) {
    companion object {
        fun createUniqueFileName(): String {
            var name = UUID.randomUUID().toString()
            while (File(name).exists()) {
                name = UUID.randomUUID().toString()
            }

            return "$name.bin"
        }
    }

    private fun saveFile(filename: String, data: ByteArray) {
        try {
            val file = File(filename)

            file.createNewFile() // ensure the file exists

            val bufferedStream = BufferedOutputStream(FileOutputStream(file))

            bufferedStream.write(data)

            bufferedStream.flush()
            bufferedStream.close()
        }
        catch (e: Exception) {
            throw IOException() // generalize any exception to IOException to make exception handlers simpler
        }
    }

    fun getFileContent(fileId: Long): ByteArray {
        val fileEntry = fileRepository.findByIdOrNull(fileId) ?: throw NoSuchFileException()
        try {
            val file = File(fileEntry.name)
            if (!file.exists()) {
                throw Exception()
            }

            val bufferedStream = BufferedInputStream(FileInputStream(file))
            val fileContent = bufferedStream.readAllBytes()

            bufferedStream.close()

            return fileContent
        }
        catch (e: Exception) {
            throw IOException() // generalize any exception to IOException to make exception handlers simpler
        }
    }

    fun getFileName(fileId: Long): String {
        val fileEntry = fileRepository.findByIdOrNull(fileId) ?: throw NoSuchFileException()

        return fileEntry.name
    }

    fun getContentType(fileId: Long): String {
        val fileEntry = fileRepository.findByIdOrNull(fileId) ?: throw NoSuchFileException()
        return Files.probeContentType(Path.of(fileEntry.path))
    }


    fun uploadFile(filename: String, data: ByteArray): com.stsf.globalbackend.models.File {
        val path = createUniqueFileName()
        saveFile(path, data)

        return fileRepository.save(com.stsf.globalbackend.models.File(-1, filename, path))
    }


}