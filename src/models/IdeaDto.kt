package com.tribuna.models

import java.util.*

class IdeaDto (
    val id:Int?,
    val authorName:String,
    val ideaText:String,
    val ideaDate: Date,
    val link:String?,
    val ideaDrawable:ByteArray
)