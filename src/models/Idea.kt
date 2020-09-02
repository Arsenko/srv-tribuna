package com.tribuna.models

import java.util.*

class Idea (
    val id:Int?,
    val authorName:String,
    val ideaText:String,
    val ideaDate: Date,
    val link:String,
    val ideaDrawableLink:String,
    val ideaSupportUsernames:MutableList<String>
)