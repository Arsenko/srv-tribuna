package com.tribuna.repos
import com.example.tribuna.models.IdeaData
import com.tribuna.models.CounterChangeDto
import com.tribuna.models.Idea
import com.tribuna.models.IdeaDto
import com.tribuna.models.UserReaction
import io.ktor.http.HttpStatusCode

interface IdeaRepository{
    suspend fun getAll(): List<Idea>
    suspend fun changeIdeaCounter(model: CounterChangeDto, login:String): Idea
    suspend fun addIdea(idea: IdeaData,username:String): Boolean
    suspend fun getIdeaReactionsById(id:Int): List<UserReaction>
    suspend fun deleteById(id:Int,authorName:String):HttpStatusCode
    suspend fun getIdeaWithAuthor(authorName:String):MutableList<Idea>
}