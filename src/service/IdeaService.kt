package com.tribuna.service

import com.tribuna.repos.IdeaRepository
import com.tribuna.models.CounterChangeDto
import com.tribuna.models.IdeaDto
import io.ktor.http.HttpStatusCode

class IdeaService(private val repos: IdeaRepository) {
    suspend fun getAll(username:String): List<IdeaDto?> {
        val temp =repos.getAll(username)
        return temp
    }
    suspend fun getById(id:Int,username: String): IdeaDto? {
        return repos.getById(id,username)
    }
    suspend fun deleteById(id:Int,authorName: String):HttpStatusCode{
        return repos.deleteById(id,authorName)
    }
    suspend fun addIdea(idea: IdeaDto):HttpStatusCode{
        return repos.addIdea(idea)
    }
    suspend fun changeCounter(model: CounterChangeDto, login: String): IdeaDto? {
        return repos.changeIdeaCounter(model,login)
    }
}