package com.tribuna.models

class Author(
        val authorName:String,
        var authorDrawable: ByteArray,
        var authorStatus:Int // like>dislike*2, like>1=promouter
) {
}