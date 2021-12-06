package com.stsf.globalbackend.controllers

import com.stsf.globalbackend.models.File
import com.stsf.globalbackend.request.GenericResponse
import com.stsf.globalbackend.services.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.activation.MimeType
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/api/file")
class FileController(
    @Autowired
    private val fileService: FileService
) {
    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): GenericResponse<File> {
        // TODO: Add check if user is registered
        return GenericResponse(fileService.uploadFile(file))
    }

    @GetMapping("/download")
    @ResponseBody
    fun downloadFile(@RequestParam fileId: Long, request: HttpServletRequest): ResponseEntity<Resource> {
        val file = fileService.getFile(fileId)
        val resource = FileSystemResource(file)

        val mimeType: MediaType = MediaType.parseMediaType(request.servletContext.getMimeType(file.absolutePath) ?: MediaType.APPLICATION_OCTET_STREAM.toString())

        return ResponseEntity.ok()
            .contentLength(file.length())
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileService.getFileName(fileId) + "\"")
            .contentType(mimeType)
            .body(resource)
    }

    @GetMapping("/type")
    fun getFileExtension(@RequestParam fileId: Long): GenericResponse<String> {
        return GenericResponse(fileService.getFileExtension(fileService.getFileName(fileId)))
    }

}
