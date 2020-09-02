package com.tribuna.routing

import com.minnullin.models.*
import com.tribuna.models.AuthenticationInDto
import com.minnullin.service.IdeaService
import com.minnullin.service.UserService
import com.tribuna.models.IdeaDto
import com.tribuna.models.User
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.features.ParameterConversionException
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

class RoutingV1(
                private val postService: IdeaService,
                private val userService: UserService
) {
    fun setup(configuration: Routing){
        with(configuration){
            route("/api/v1/") {
                post("/registration") {
                    val input=call.receive<AuthenticationInDto>()
                    val response=userService.registration(input)
                    if(response!=null){
                        call.respond(response)
                    }else{
                        call.respond("username already exist")
                    }
                }

                post("/authentication") {
                    val input = call.receive<AuthenticationInDto>()
                    val response = userService.authenticate(input)
                    call.respond(response)
                }
            }
            authenticate {
                route("/api/v1/idea/") {
                    get {
                        val respond = postService.getAll(call.authentication.principal<User>()!!.name)
                        call.respond(respond)
                    }
                }
                route("/api/v1/idea/") {
                    post {
                        val input = call.receive<IdeaDto>()
                        postService.addIdea(input)
                        call.respond(HttpStatusCode.Accepted)
                    }
                }

                route("/api/v1/idea/{id}") {
                    get {
                        val id = call.parameters["id"]?.toIntOrNull() ?: throw ParameterConversionException(
                            "id",
                            "Int"
                        )
                        val response = postService.getById(id)
                        if (response != null) {
                            call.respond(response)
                        } else {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }

                route("/api/v1/posts/{id}/delete") {
                    get {
                        val id = call.parameters["id"]?.toIntOrNull() ?: throw ParameterConversionException(
                            "id",
                            "Int"
                        )
                        call.respond(postService.deleteById(id, call.authentication.principal<User>()!!.name))
                    }
                }

                route("/api/v1/idea/changeCounter") {
                    post {
                        val receiveModel: CounterChangeDto = call.receive()
                        receiveModel.let { outerIt ->
                            postService.changeCounter(outerIt,call.authentication.principal<User>()!!.name).let {
                                if (it != null) {
                                    call.respond(it)
                                } else {
                                    call.respond(HttpStatusCode.BadRequest)
                                }
                            }

                        }
                    }
                }
            }
            route("/") {
                get {
                    call.respond(HttpStatusCode.Accepted, "test completed")
                }
            }
        }
    }
}