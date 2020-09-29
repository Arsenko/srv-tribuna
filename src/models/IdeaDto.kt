package com.tribuna.models

import java.util.*

class IdeaDto(
        val id: Int,
        val author:AuthorDto,
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
            return if(idea.id!=null) {
                if (temp != null) {
                    if (temp.promoted) {
                        IdeaDto(idea.id, AuthorDto.generateDto(author),idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, true, false)
                    }else{
                        IdeaDto(idea.id, AuthorDto.generateDto(author) , idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, true)
                    }
                }else{
                    IdeaDto(idea.id, AuthorDto.generateDto(author) , idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, false)
                }
            }else {
                IdeaDto(-1,AuthorDto.generateDto(author) , idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, false)
            }
        }
        fun generate(idea: Idea, userName: String,authorlist:List<Author>): IdeaDto {
            val temp=idea.ideaReaction.find{
                it.authorName==userName
            }
            val authorFind=authorlist.find{
                it.authorName==userName
            }
            return if(idea.id!=null && authorFind!=null) {
                if (temp != null) {
                    if (temp.promoted) {
                        IdeaDto(idea.id, AuthorDto.generateDto(authorFind),idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, true, false)
                    }else{
                        IdeaDto(idea.id, AuthorDto.generateDto(authorFind) , idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, true)
                    }
                }else{
                    IdeaDto(idea.id, AuthorDto.generateDto(authorFind) , idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, false)
                }
            }else {
                IdeaDto(-1,AuthorDto.generateDto(authorFind!!) , idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, false, false)
            }
        }
    }
}