package com.tribuna.repos

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

    override suspend fun getAll(username: String): List<IdeaDto> {
        val temp = idealist.toList()
        return temp.map { Idea.generateModel(it, username) }
    }

    private suspend fun getAutoIncrementedId(): Int {
        mutex.withLock {
            id++
            return id
        }
    }

    override suspend fun addIdea(idea: IdeaDto): HttpStatusCode {
        val ideaWithId = Idea(
                id = getAutoIncrementedId(),
                authorName = idea.authorName,
                ideaText = idea.ideaText,
                ideaDate = idea.ideaDate,
                link = idea.link,
                ideaDrawable = idea.ideaDrawable,
                ideaReaction = mutableListOf<UserReaction>()
        )
        idealist.add(ideaWithId)
        return HttpStatusCode.Accepted
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

    override suspend fun getById(id: Int, username: String): IdeaDto {
        mutex.withLock {
            var ideaToReturn: Idea? = null
            for (i in 0 until idealist.size) {
                if (id == idealist[i].id) {
                    ideaToReturn = idealist[i]
                }
            }
            if (ideaToReturn == null) {
                throw NotFoundException()
            }
            return Idea.generateModel(ideaToReturn, username)
        }
    }

    override suspend fun getIdeaWithAuthor(authorName: String): List<Idea> {
        var temp = idealist.filter{
            it.authorName.contentEquals(authorName)
        }
        return temp
    }

    override suspend fun changeIdeaCounter(model: CounterChangeDto, login: String): IdeaDto {
        mutex.withLock {
            return getById(model.id, login)
        }
    }
}