package com.tribuna.service

import com.example.tribuna.models.IdeaData
import com.tribuna.repos.IdeaRepository
import com.tribuna.models.CounterChangeDto
import com.tribuna.models.Idea
import com.tribuna.models.IdeaDto
import com.tribuna.models.UserReaction
import io.ktor.http.HttpStatusCode

class IdeaService(private val repos: IdeaRepository) {
    suspend fun getAll(username:String): List<IdeaDto?> {
        return repos.getAll(username)
    }
    suspend fun getById(id:Int,username: String): IdeaDto? {
        return repos.getById(id,username)
    }
    suspend fun getIdeaReactionsById(id:Int):List<UserReaction>{
        return repos.getIdeaReactionsById(id)
    }
    suspend fun deleteById(id:Int,authorName: String):HttpStatusCode{
        return repos.deleteById(id,authorName)
    }
    suspend fun addIdea(idea: IdeaData, username:String):Boolean{
        return repos.addIdea(idea,username)
    }
    suspend fun changeCounter(model: CounterChangeDto, login: String): IdeaDto? {
        return repos.changeIdeaCounter(model,login)
    }
    suspend fun getIdeasWithAuthor(authorName: String):List<Idea>{
        return repos.getIdeaWithAuthor(authorName)
    }
}