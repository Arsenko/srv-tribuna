package com.tribuna.service

import com.example.tribuna.models.ChangeProfile
import com.tribuna.exceptions.AlreadyExistException
import com.tribuna.exceptions.InvalidPasswordException
import com.tribuna.exceptions.PasswordChangeException
import com.tribuna.repos.UserRepository
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

    suspend fun getAutorList(): List<Author>{
        return repos.getAuthorList()
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
        }else {
            this.authenticate(input)
        }
    }

    suspend fun save(username: String, password: String):String {
        mutex.withLock {
            repos.save(User(username = username, password = passwordEncoder.encode(password),author = Author(username, byteArrayOf(),0)))
            return tokenService.generate(repos.getByUsername(username)!!.id)
        }
    }

    suspend fun save(username: String,password: String,author: Author):String{
        mutex.withLock {
            repos.save(User(username = username, password = passwordEncoder.encode(password),author = author))
            return tokenService.generate(repos.getByUsername(username)!!.id)
        }
    }

    suspend fun getAuthorByUsername(username: String):Author{
        mutex.withLock {
            return repos.getAuthor(username)
        }
    }

    suspend fun changeUserData(username:String,change: ChangeProfile):Boolean{
        mutex.withLock {
            val model = repos.getByUsername(username) ?: throw NotFoundException()
            if (!passwordEncoder.matches(change.authorNewPass, model.password)) {
                throw PasswordChangeException("Wrong password!")
                return false
            }else{
                return repos.changeData(username,change)
            }
        }
    }

    suspend fun getByUsername(username: String):User?{
        mutex.withLock {
            return repos.getByUsername(username)
        }
    }
}