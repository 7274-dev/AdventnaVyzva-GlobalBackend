package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.models.File
import com.stsf.globalbackend.request.GenericResponse
import com.stsf.globalbackend.services.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.spi.FileTypeDetector

@RestController("/api/file")
class FileController(
    @Autowired
    private val fileService: FileService
) {
    @PostMapping("/upload")
    fun uploadFile(@RequestBody file: com.stsf.globalbackend.request.File): GenericResponse<File> {
        return GenericResponse(fileService.uploadFile(file.filename, file.data))
    }

    @GetMapping("/download")
    fun downloadFile(@RequestParam fileId: Long): RawFile {
        val contentType = fileService.getContentType(fileId)

        val fileContent = fileService.getFileContent(fileId)
        val filename = fileService.getFileName(fileId)

        return RawFile(fileContent, filename, contentType)
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