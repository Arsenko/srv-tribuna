package com.tribuna.models

class Author(
    val authorName:String,
    val authorDrawable: ByteArray,
    val authorStatus:Boolean //false=hater, null=neutral, true=promoter
) {
}