package com.tribuna.repos

import com.tribuna.models.Author
import com.tribuna.models.User
import com.tribuna.models.UserDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserRepositoryBasic : UserRepository {
    private val authorRepository=AuthorRepositoryBasic()
    private var index: Int = 3
    private val users = mutableListOf(
            User(0, "Mars", "Mars"),
            User(1, "Kitkat", "kitkat"),
            User(2, "Milka", "Milka"),
            User(3, "Twix", "Twix")
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
            return authorRepository.getAuthorByUsername(username)
        }
    }
}