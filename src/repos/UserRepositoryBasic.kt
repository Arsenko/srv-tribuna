package com.tribuna.repos

import com.tribuna.models.Author
import com.tribuna.models.User
import com.tribuna.models.UserDto
import io.ktor.features.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserRepositoryBasic : UserRepository {
    private var index: Int = 3
    private val users = mutableListOf(
            User(0, "Mars", "Mars",Author("Mars", byteArrayOf(), 4)),
            User(1, "Kitkat", "kitkat",Author("Kitkat", byteArrayOf(), -7)),
            User(2, "Milka", "Milka",Author("Milka", byteArrayOf(), 16)),
            User(3, "Twix", "Twix",Author("Twix", byteArrayOf(), -28))
    )
    private val mutex = Mutex()
    override suspend fun getAll(): List<User> {
        return users.toList()
    }

    override suspend fun getById(id: Int): User? {
        mutex.withLock {
            return users.find {
                it.id == id
            }
        }
    }

    override suspend fun getByUsername(username: String): User? {
        mutex.withLock {
            return users.find {
                it.username == username
            }
        }
    }

    override suspend fun save(item: User): UserDto { //for addUser and changePass
        mutex.withLock {
            val returnVal = UserDto(index, item.username)
            users.add(item)
            return returnVal
        }
    }

    override suspend fun getAuthor(username: String): Author? {
        mutex.withLock {
            val temp= users.find{
                it.username==username
            }?.author
            if (temp!=null){
                return temp
            }else{
                throw NotFoundException()
            }
        }
    }

    override suspend fun getAuthorList(): List<Author> {
        var result= mutableListOf<Author>()
        for(i in users.indices){
            users[i].author?.let { result.add(it) }
        }
        return result.toList()
    }
}