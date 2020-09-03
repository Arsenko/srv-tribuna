package com.tribuna.models

import java.util.*

class Idea(
        val id: Int?,
        val authorName: String,
        val ideaText: String,
        val ideaDate: Date,
        val link: String?,
        val ideaDrawable: ByteArray,
        val ideaSupportUsernames: MutableList<String>,
        val ideaHaterUsernames: MutableList<String>
) {
    companion object {
        fun generateModel(idea: Idea, userName: String): IdeaDto {
            val likeFlag = idea.ideaSupportUsernames.contains(userName)
            val dislikeFlag = idea.ideaHaterUsernames.contains(userName)
            return if(idea.id!=null) {
                IdeaDto(idea.id, idea.authorName, idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, likeFlag, dislikeFlag)
            }else{
                IdeaDto(-1, idea.authorName, idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, likeFlag, dislikeFlag)
            }
        }
    }
}