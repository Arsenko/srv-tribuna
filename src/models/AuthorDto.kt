package com.tribuna.models

class AuthorDto (
    val authorName:String,
    val authorDrawable: ByteArray,
    val authorStatus:Boolean?
){
    companion object{
        fun generateDto(author:Author):AuthorDto{
            val status=when(author.authorStatus){
                in 26..2147483647 -> true
                in -2147483648..-26 -> false
                in 25..25 -> null
                else -> null
            }
            return AuthorDto(author.authorName,author.authorDrawable,status)
        }
    }
}