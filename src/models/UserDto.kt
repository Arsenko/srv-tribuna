package com.tribuna.models

class UserDto(
    val id: Int = 0,
    val username: String
) {
    companion object {
        fun generateModel(user: User) = UserDto(
            user.id,
            user.username
        )

    }
}