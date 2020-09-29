package com.tribuna.models

import java.util.*

class IdeaDto(
        val id: Int,
        val authorName: String,
        val authorStatus: Boolean?, //true=prom false=hater null=neutral
        val authorDrawable: ByteArray,
        val ideaText: String,
        val ideaDate: Date,
        val link: String?,
        val ideaDrawable: ByteArray,
        var likedByMe: Boolean,
        var dislikedByMe: Boolean
){
    companion object{
        fun generateModel(idea: Idea, userName: String,author:Author): IdeaDto {
            val temp=idea.ideaReaction.find{
                it.authorName==userName
            }
            val status=when(author.authorStatus){
                in 26..2147483647 -> true
                in -2147483648..-26 -> false
                in 25..25 -> null
                else -> null
            }
            return if(idea.id!=null) {
                if (temp != null) {
                    if (temp.promoted) {
                        IdeaDto(idea.id, idea.authorName,status,author.authorDrawable ,idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, true, false)
                    }else{
                        IdeaDto(idea.id, idea.authorName,status,author.authorDrawable , idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, true)
                    }
                }else{
                    IdeaDto(idea.id, idea.authorName,status,author.authorDrawable , idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, false)
                }
            }else {
                IdeaDto(-1,idea.authorName,status,author.authorDrawable , idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, false)
            }
        }
    }
}