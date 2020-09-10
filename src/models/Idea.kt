package com.tribuna.models

import java.util.*

class Idea(
        val id: Int?,
        val authorName: String,
        val ideaText: String,
        val ideaDate: Date,
        val link: String?,
        val ideaDrawable: ByteArray,
        val ideaReaction: MutableList<UserReaction>
) {
    companion object {
        fun generateModel(idea: Idea, userName: String): IdeaDto {
            val temp=idea.ideaReaction.find{
                it.authorName==userName
            }
            return if(idea.id!=null) {
                if (temp != null) {
                    if (temp.promoted) {
                        IdeaDto(idea.id, idea.authorName, idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, true, false)
                    }else{
                        IdeaDto(idea.id, idea.authorName, idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, true)
                    }
                }else{
                    IdeaDto(idea.id, idea.authorName, idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, false)
                }
            }else {
                IdeaDto(-1, idea.authorName, idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, false)
            }
        }
    }
}