package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.models.File
import com.stsf.globalbackend.request.GenericResponse
import com.stsf.globalbackend.services.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.spi.FileTypeDetector
import java.util.*

@RestController
@RequestMapping("/api/file")
class FileController(
    @Autowired
    private val fileService: FileService
) {
    @PostMapping("/upload")
    fun uploadFile(@RequestBody file: MultipartFile): GenericResponse<File> {
        // TODO: Add check if user is registered
        return GenericResponse(fileService.uploadFile(file))
    }

    @GetMapping("/download")
    @ResponseBody
    fun downloadFile(@RequestParam fileId: Long): FileSystemResource {
        // TODO: Add check if user is registered
        val contentType = fileService.getContentType(fileId)

        val fileContent = fileService.getFileContent(fileId)
        val filename = fileService.getFileName(fileId)

        return FileSystemResource(fileService.getFilePath(fileId))
    }

}

class RawFile(
    private val data: ByteArray,
    private val filename: String,
    private val contentType: String
) : MultipartFile {
    override fun getInputStream(): InputStream {
        return ByteArrayInputStream(data)
    }

    override fun getName(): String {
        return filename
    }

    override fun getOriginalFilename(): String {
        return filename
    }

    override fun getContentType(): String {
        return contentType
    }

    override fun isEmpty(): Boolean {
        return data.isEmpty()
    }

    override fun getSize(): Long {
        return data.size.toLong()
    }

    override fun getBytes(): ByteArray {
        return data
    }

    override fun transferTo(dest: java.io.File) {
        dest.createNewFile()

        val outputStream = FileOutputStream(dest)

        outputStream.write(data)

        outputStream.flush()
        outputStream.close()
    }

}