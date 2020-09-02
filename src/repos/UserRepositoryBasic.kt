package com.minnullin.repos

import com.tribuna.models.User
import com.tribuna.models.UserDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserRepositoryBasic:UserRepository {
    private var index:Int=0
    private val users= mutableListOf<User>()
    private val mutex= Mutex()
    override suspend fun getAll(): List<User> {
        return users.toList()
    }

    override suspend fun getById(id: Int): User? {
        mutex.withLock {
            return users.find {
                it.id==id
            }
        }
    }

    override suspend fun getByUsername(username: String): User? {
        mutex.withLock {
            return users.find{
                it.username==username
            }
        }
    }

    override suspend fun save(item: User): UserDto { //for addUser and changePass
        mutex.withLock {
            val returnVal= UserDto(index,item.username)
            users.add(item)
            return returnVal
        }
    }
}