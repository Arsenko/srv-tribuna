package com.tribuna.models

import java.security.Principal

data class User(
        val id: Int = 0,
        var username: String,
        var password: String,
        val author: Author?
): Principal, io.ktor.auth.Principal {
    override fun getName(): String {
        return username
    }
}