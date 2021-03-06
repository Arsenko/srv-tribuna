package com.tribuna.models

import java.util.*

class UserReactionDto (
        val author: AuthorDto,
        val promoted:Boolean?,
        val date: Date
)

class UserReaction(
        val authorName: String,
        val promoted:Boolean,
        val date: Date = Date()
) {
    companion object {
        fun generateListOfDto(listOfReaction: List<UserReaction>, listOfAuthors:List<Author>):List<UserReactionDto>{
            var result:MutableList<UserReactionDto>?=mutableListOf()
            for(i in listOfReaction.indices){
                var temp=listOfAuthors.find{
                    it.authorName==listOfReaction[i].authorName
                }
                if(temp!=null){
                    result!!.add(UserReactionDto(AuthorDto.generateDto(temp),listOfReaction[i].promoted,listOfReaction[i].date))
                }
            }
            return result!!.toList()
        }
    }

}
