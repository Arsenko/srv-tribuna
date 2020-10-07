package com.tribuna.repos

import com.example.tribuna.models.ChangeProfile
import com.tribuna.models.Author
import com.tribuna.models.User
import com.tribuna.models.UserDto
import io.ktor.features.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserRepositoryBasic : UserRepository {
    private var index: Int = 0
    private var users = mutableListOf<User>(/*
            User(0, "Mars", "Mars",Author("Mars", byteArrayOf(), 4)),
            User(1, "Kitkat", "kitkat",Author("Kitkat", byteArrayOf(), -7)),
            User(2, "Milka", "Milka",Author("Milka", byteArrayOf(), 16)),
            User(3, "Twix", "Twix",Author("Twix", byteArrayOf(), -28))*/
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

    override suspend fun getByUsername(username: String): User {
        mutex.withLock {
            val name = username.replace("\"", "")
            val userfind = users.find {
                it.username == username
            }
            if(userfind!=null){
                return userfind
            }else{
                throw NotFoundException()
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

    override suspend fun getAuthorList(): List<Author> {
        var result= mutableListOf<Author>()
        for(i in 0 until users.size){
            users[i].author?.let { result.add(it) }
        }
        return result.toList()
    }

    override suspend fun changeData(username:String,change: ChangeProfile):Boolean{
        var result=false
        for(i in 0 until users.size){
            if(username==users[i].username){
                if(change.authorName!=null) {
                    users[i].username = change.authorName
                }
                if(change.authorNewPass!=null){
                    users[i].password = change.authorNewPass
                }
                if(change.authorDrawable!=null){
                    users[i].author!!.authorDrawable=change.authorDrawable
                }
                result=true
            }
        }
        return result
    }
}