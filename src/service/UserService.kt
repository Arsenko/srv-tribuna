package com.minnullin.service

import JWTTokenService
import com.tribuna.exceptions.AlreadyExistException
import com.tribuna.exceptions.InvalidPasswordException
import com.tribuna.exceptions.PasswordChangeException
import com.minnullin.repos.UserRepository
import com.tribuna.models.*
import io.ktor.features.NotFoundException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.security.crypto.password.PasswordEncoder

val mutex= Mutex()

class UserService(
    private val repos: UserRepository,
    private val tokenService: JWTTokenService,
    private val passwordEncoder: PasswordEncoder
) {

    suspend fun getModelById(id: Int): User? {
        return repos.getById(id)
    }

    suspend fun getById(id: Int): UserDto {
        val model = repos.getById(id) ?: throw NotFoundException()
        return UserDto.generateModel(model)
    }

    suspend fun changePassword(id: Int, input: PasswordChangeDto) {
        mutex.withLock {
            val model = repos.getById(id) ?: throw NotFoundException()
            if (!passwordEncoder.matches(input.old, model.password)) {
                throw PasswordChangeException("Wrong password!")
            }
            val copy = model.copy(password = passwordEncoder.encode(input.new))
            repos.save(copy)
        }
    }

    suspend fun authenticate(input: AuthenticationInDto): AuthenticationOutDto {
        val model = repos.getByUsername(input.username) ?: throw NotFoundException()
        if (!passwordEncoder.matches(input.password, model.password)) {
            throw InvalidPasswordException("Wrong password!")
        }

        val token = tokenService.generate(model.id)
        return AuthenticationOutDto(token)
    }

    suspend fun registration(input: AuthenticationInDto): AuthenticationOutDto?{
        return if(repos.getByUsername(input.username)!=null){
            throw AlreadyExistException("username already exist")
            null
        }else {
            this.save(input.username,input.password)
            return this.authenticate(input)
        }
    }

    suspend fun save(username: String, password: String):String {
        mutex.withLock {
            repos.save(User(username = username, password = passwordEncoder.encode(password)))
            return tokenService.generate(repos.getByUsername(username)!!.id)
        }
    }


}