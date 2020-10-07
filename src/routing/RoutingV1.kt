package com.tribuna.routing

import com.example.tribuna.models.ChangeProfile
import com.example.tribuna.models.IdeaData
import com.tribuna.models.*
import com.tribuna.service.IdeaService
import com.tribuna.service.UserService
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
        private val ideaService: IdeaService,
        private val userService: UserService
) {
    fun setup(configuration: Routing) {
        with(configuration) {
            route("/api/v1/") {
                post("/registration") {
                    val input = call.receive<AuthenticationInDto>()
                    val response = userService.registration(input)
                    if (response != null) {
                        call.respond(response)
                    } else {
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
                        val idealist = ideaService.getAll()
                        val authorlist = userService.getAutorList()
                        call.respond(idealist.map {
                            IdeaDto.generate(it, call.authentication.principal<User>()!!.name, authorlist)
                        })
                    }
                }

                route("/api/v1/idea/{id}/delete") {
                    get {
                        val id = call.parameters["id"]?.toIntOrNull() ?: throw ParameterConversionException(
                                "id",
                                "Int"
                        )
                        call.respond(ideaService.deleteById(id, call.authentication.principal<User>()!!.name))
                    }
                }

                route("/api/v1/author/") {
                    get {
                        val username = call.authentication.principal<User>()!!.name
                        val temp = userService.getAuthorByUsername(username)
                        call.respond(temp)
                    }
                }

                route("/api/v1/idea/changeCounter") {
                    post {
                        val receiveModel: CounterChangeDto = call.receive()
                        val author = userService.getAuthorByUsername(call.authentication.principal<User>()!!.name)
                        receiveModel.let { outerIt ->
                            ideaService.changeCounter(outerIt, call.authentication.principal<User>()!!.name).let {
                                if (it != null) {
                                    call.respond(IdeaDto.generateModel(it, call.authentication.principal<User>()!!.name, author))
                                } else {
                                    call.respond(HttpStatusCode.BadRequest)
                                }
                            }

                        }
                    }
                }

                route("api/v1/idea/authorReactions") {
                    post {
                        val receive: Int = call.receive()
                        val listOfReactions = ideaService.getIdeaReactionsById(receive)
                        val listOfAuthors = userService.getAutorList()
                        call.respond(UserReaction.generateListOfDto(listOfReactions, listOfAuthors))
                    }
                }

                route("api/v1/idea/withAuthor") {
                    post {
                        val receive: AuthorNameDto = call.receive()
                        call.respond(ideaService.getIdeasWithAuthor(receive.name))
                    }
                }

                route("api/v1/user/change") {
                    post {
                        val receive: ChangeProfile = call.receive()
                        call.respond(userService.changeUserData(call.authentication.principal<User>()!!.name, receive))
                    }
                }

                route("api/v1/idea/add") {
                    post {
                        val receive: IdeaData = call.receive()
                        call.respond(ideaService.addIdea(receive, call.authentication.principal<User>()!!.name))
                    }
                }

                route("api/v1/user/author") {
                    get {
                        call.respond(AuthorDto.generateDto(userService.getAuthorByUsername("Milka")))
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