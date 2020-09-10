package com.tribuna.com.tribuna.models

import com.tribuna.models.Author
import java.util.*

class UserReactionDto (
    val author: Author,
    val promoted:Boolean,
    val date: Date
)
