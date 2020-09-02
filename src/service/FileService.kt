package com.minnullin.service

import com.minnullin.models.MediaResponseDto
import com.minnullin.models.MediaType
import io.ktor.features.BadRequestException
import io.ktor.features.UnsupportedMediaTypeException
import io.ktor.http.ContentType
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import io.ktor.utils.io.core.use as coreUse

class FileService(private val imagePath:String) {
    private val images=listOf(ContentType.Image.JPEG,ContentType.Image.PNG)

    init {
        println(Paths.get(imagePath).toAbsolutePath().toString())
        if (Files.notExists(Paths.get(imagePath))) {
            Files.createDirectory(Paths.get(imagePath))
        }
    }

    suspend fun save(multipart: MultiPartData): MediaResponseDto {
        var response: MediaResponseDto? = null
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    if (part.name == "file") {
                        if (!images.contains(part.contentType)) {
                            throw UnsupportedMediaTypeException(part.contentType ?: ContentType.Any)
                        }
                        val ext = when (part.contentType) {
                            ContentType.Image.JPEG -> "jpg"
                            ContentType.Image.PNG -> "png"
                            else -> throw UnsupportedMediaTypeException(part.contentType!!)
                        }
                        val name = "${UUID.randomUUID()}.$ext"
                        val path = Paths.get(imagePath, name)
                        part.streamProvider().coreUse {
                             withContext(Dispatchers.IO){
                                Files.copy(it, path)
                            }
                        }
                        part.dispose()
                        response = MediaResponseDto(name, MediaType.Image,File("$imagePath/$name"))
                        return@forEachPart
                    }
                }
                is PartData.FormItem -> TODO()
                is PartData.BinaryItem -> TODO()
            }

            part.dispose()
        }
        return response ?: throw BadRequestException("No file field in request")
    }
}