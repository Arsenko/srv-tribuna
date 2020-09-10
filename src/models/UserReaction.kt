package com.tribuna.models

import com.tribuna.com.tribuna.models.UserReactionDto
import java.util.*

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
                    result!!.add(UserReactionDto(temp,listOfReaction[i].promoted,listOfReaction[i].date))
                }
            }
            return result!!.toList()
        }
    }

}