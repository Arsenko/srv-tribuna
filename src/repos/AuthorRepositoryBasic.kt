package com.tribuna.repos

import com.tribuna.models.Author
import io.ktor.features.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthorRepositoryBasic : AuthorRepository {
    private var list = mutableListOf(
            Author("Mars", byteArrayOf(), 4),
            Author("Kitkat", byteArrayOf(), -7),
            Author("Milka", byteArrayOf(), 16),
            Author("Twix", byteArrayOf(), -28)
    )

    private val mutex = Mutex()

    override suspend fun getAuthors(): List<Author> {
        return list.toList()
    }

    override suspend fun getAuthorByUsername(username: String): Author? {
        mutex.withLock {
            return list.find {
                it.authorName == username
            }
        }
    }

    override suspend fun updateStatus(username: String, increase: Boolean): Author {
        mutex.withLock {
            var temp: Author? = null
            var index = -1
            for (i in 0 until list.size) {
                if (list[i].authorName == username) {
                    index = i
                    temp = list[i]
                }
            }
            return if (index != -1 && temp != null) {
                if(increase) {
                    list[index] = Author(
                            temp.authorName, temp.authorDrawable, temp.authorStatus++
                    )
                }else{
                    list[index] = Author(
                            temp.authorName, temp.authorDrawable, temp.authorStatus--
                    )
                }
                list[index]
            }else{
                throw NotFoundException()
                //return Author("", byteArrayOf(),0)
            }
        }
    }
}