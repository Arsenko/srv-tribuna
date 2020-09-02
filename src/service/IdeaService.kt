package com.minnullin.service

import IdeaRepository
import com.minnullin.models.*
import com.tribuna.models.IdeaDto
import io.ktor.http.HttpStatusCode

class IdeaService(private val repos: IdeaRepository) {
    suspend fun getAll(login:String): List<IdeaDto?> {
        val temp =repos.getAll()
        return temp
    }
    suspend fun getById(id:Int): IdeaDto? {
        return repos.getById(id)
    }
    suspend fun deleteById(id:Int,authorName: String):HttpStatusCode{
        return repos.deleteById(id,authorName)
    }
    suspend fun addIdea(idea: IdeaDto):HttpStatusCode{
        return repos.addIdea(idea)
    }
    suspend fun changeCounter(model: CounterChangeDto,login: String): IdeaDto? {
        return repos.changeIdeaCounter(model,login)
    }
}