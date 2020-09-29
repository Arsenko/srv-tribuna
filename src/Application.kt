package com.tribuna

import com.tribuna.models.Author
import com.tribuna.models.User
import com.tribuna.repos.IdeaRepository
import com.tribuna.repos.IdeaRepositoryBasic
import com.tribuna.service.JWTTokenService
import com.tribuna.repos.UserRepository
import com.tribuna.repos.UserRepositoryBasic
import com.tribuna.service.IdeaService
import com.tribuna.service.UserService
import com.tribuna.routing.RoutingV1
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.runBlocking
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance
import org.kodein.di.ktor.KodeinFeature
import org.kodein.di.ktor.kodein
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(KodeinFeature) {
        bind<IdeaService>() with eagerSingleton { IdeaService(instance()) }
        bind<IdeaRepository>() with eagerSingleton { IdeaRepositoryBasic() }
        bind<PasswordEncoder>() with eagerSingleton { BCryptPasswordEncoder() }
        bind<JWTTokenService>() with eagerSingleton { JWTTokenService() }
        bind<UserRepository>() with eagerSingleton { UserRepositoryBasic() }
        bind<UserService>() with eagerSingleton {
            UserService(instance(), instance(), instance()).apply {
                runBlocking {
                    this@apply.save("aaa", "111")
                    this@apply.save("Mars", "Mars", Author("Mars", byteArrayOf(), 4))
                    this@apply.save("Kitkat", "kitkat", Author("Kitkat", byteArrayOf(), -7))
                    this@apply.save("Milka", "Milka", Author("Milka", byteArrayOf(), 16))
                    this@apply.save("Twix", "Twix", Author("Twix", byteArrayOf(), -28))
                }
            }
        }
        bind<RoutingV1>() with eagerSingleton {
            RoutingV1(
                    instance(),
                    instance()
            )
        }
    }

    install(Authentication) {
        jwt {
            val jwtService by kodein().instance<JWTTokenService>()
            verifier(jwtService.verifier)
            val userService by kodein().instance<UserService>()

            validate {
                val id = it.payload.getClaim("id").asInt()
                userService.getModelById(id)
            }
        }
    }

    install(StatusPages) {
        exception<NotFoundException> { e ->
            call.respond(HttpStatusCode.NotFound)
            throw e
        }
        exception<ParameterConversionException> { e ->
            call.respond(HttpStatusCode.BadRequest)
            throw e
        }
        exception<Throwable> { e ->
            call.respond(HttpStatusCode.InternalServerError)
            throw e
        }
    }

    install(Routing) {
        val routingV1 by kodein().instance<RoutingV1>()
        routingV1.setup(this)
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
        }
    }
}

