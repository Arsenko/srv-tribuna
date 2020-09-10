package com.tribuna.repos

import com.tribuna.models.Author
import com.tribuna.models.User
import com.tribuna.models.UserDto

interface UserRepository {
    suspend fun getAll(): List<User>
    suspend fun getById(id: Int): User?
    suspend fun getByUsername(username: String): User?
    suspend fun save(item: User): UserDto
    suspend fun getAuthor(username: String): Author?
    suspend fun getAuthorList():List<Author>
}