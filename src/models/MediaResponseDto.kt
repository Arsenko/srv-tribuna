package com.minnullin.models

import com.sun.xml.internal.ws.api.message.Attachment
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class MediaResponseDto(
    val fileName:String,
    val type:MediaType,
    val attachment: File
)