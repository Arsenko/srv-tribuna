package com.tribuna.repos
import com.tribuna.models.CounterChangeDto
import com.tribuna.models.IdeaDto
import com.tribuna.models.UserReaction
import io.ktor.http.HttpStatusCode

interface IdeaRepository{
    suspend fun getAll(username:String): List<IdeaDto>
    suspend fun changeIdeaCounter(model: CounterChangeDto, login:String): IdeaDto
    suspend fun addIdea(idea: IdeaDto): HttpStatusCode
    suspend fun getIdeaReactionsById(id:Int): List<UserReaction>
    suspend fun deleteById(id:Int,authorName:String):HttpStatusCode
    suspend fun getById(id:Int,username: String): IdeaDto
    suspend fun getIdeaWithAuthor(userName:String,authorName:String):List<IdeaDto>
}