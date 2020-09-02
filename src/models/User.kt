package com.tribuna.models

import java.security.Principal

data class User(
    val id: Int = 0,
    val username: String,
    val password: String
): Principal, io.ktor.auth.Principal {
    override fun getName(): String {
        return username
    }
}