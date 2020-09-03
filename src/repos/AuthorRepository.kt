package com.tribuna.repos

import com.tribuna.models.Author

interface AuthorRepository {
    suspend fun getAuthors(): List<Author>
    suspend fun getAuthorByUsername(username: String): Author?
    suspend fun updateStatus(username: String, increase: Boolean): Author
}