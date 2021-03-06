package com.tribuna.repos

import com.example.tribuna.models.ChangeProfile
import com.tribuna.models.Author
import com.tribuna.models.User
import com.tribuna.models.UserDto

interface UserRepository {
    suspend fun getAll(): List<User>
    suspend fun getById(id: Int): User?
    suspend fun getByUsername(username: String): User?
    suspend fun save(item: User): UserDto
    suspend fun getAuthorList():List<Author>
    suspend fun changeData(username:String,change: ChangeProfile):Boolean
}