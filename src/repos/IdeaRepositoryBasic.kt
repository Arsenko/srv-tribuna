package com.tribuna.repos

import com.example.tribuna.models.IdeaData
import com.tribuna.models.*
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*

class IdeaRepositoryBasic : IdeaRepository {
    private var id: Int = 3
    private val mutex = Mutex()
    private var idealist = mutableListOf(
            Idea(
                    0,
                    "Mars",
                    "I'm chocolate",
                    Date(),
                    null,
                    byteArrayOf(),
                    mutableListOf(UserReaction("KitKat", true), UserReaction("Milka", false))
            ),
            Idea(
                    1,
                    "KitKat",
                    "I'm chocolate",
                    Date(),
                    null,
                    byteArrayOf(),
                    mutableListOf(UserReaction("Mars", true), UserReaction("Milka", false))
            ),
            Idea(
                    2,
                    "Milka",
                    "I'm chocolate",
                    Date(),
                    null,
                    byteArrayOf(),
                    mutableListOf(UserReaction("KitKat", true), UserReaction("Twix", false))
            ),
            Idea(
                    3,
                    "Twix",
                    "I'm chocolate",
                    Date(),
                    "https://animego.org/anime/vnuk-mudreca-k945",
                    byteArrayOf(),
                    mutableListOf(UserReaction("Milka", true), UserReaction("KitKat", false))
            )
    )

    override suspend fun getAll(): List<Idea> {
        val temp = idealist.toList()
        return temp //temp.map { Idea.generateModel(it, username) }
    }

    private suspend fun getAutoIncrementedId(): Int {
        mutex.withLock {
            id++
            return id
        }
    }

    override suspend fun getIdeaReactionsById(id: Int): List<UserReaction> {
        val temp = idealist.find {
            it.id == id
        }
        if (temp != null) {
            return temp.ideaReaction.toList()
        } else {
            throw NotFoundException()
        }
    }

    override suspend fun deleteById(id: Int, authorName: String): HttpStatusCode {
        mutex.withLock {
            val findPost = idealist.find {
                it.id == id
            }
            if (findPost != null) {
                if (findPost.authorName != authorName) {
                    return HttpStatusCode.Forbidden
                }
                for (i in 0 until idealist.size) {
                    if (id == idealist[i].id) {
                        idealist.removeAt(i)
                        return HttpStatusCode.Accepted
                    }
                }
            }
            return HttpStatusCode.NotFound
        }
    }

    override suspend fun getIdeaWithAuthor(authorName: String): MutableList<Idea> {
        var temp = mutableListOf<Idea>()
        val name = authorName.replace("\"", "")//найти почему
        return if (idealist.any {
                    it.authorName == name
                }) {
            for (i in 0 until idealist.size) {
                if (idealist[i].authorName == name) {
                    temp.add(idealist[i])
                }
            }
            temp
        } else {
            mutableListOf()
        }
    }

    override suspend fun changeIdeaCounter(model: CounterChangeDto, login: String): Idea {
        mutex.withLock {
            for(i in 0 until idealist[model.id].ideaReaction.size){
                if(login==idealist[model.id].ideaReaction[i].authorName){
                    idealist[model.id].ideaReaction.removeAt(i)
                }
            }
            idealist[model.id].ideaReaction.add(UserReaction(login,model.increase,Date()))
            return idealist[model.id]
        }
    }

    override suspend fun addIdea(idea: IdeaData, username: String): Boolean {
        val ideatoadd = Idea(getAutoIncrementedId(), username, idea.ideaText, idea.ideaDate, idea.link, idea.ideaDrawable, mutableListOf())
        idealist.add(ideatoadd)
        return true
    }
}