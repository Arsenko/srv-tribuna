package com.tribuna.service

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTTokenService {
    private final val secret = "5a2dbef6-289c-46e6-8cfd-d8b3292d373a"
    private val algo = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT.require(algo).build()

    fun generate(id: Int): String = JWT.create()
        .withClaim("id", id)
        .sign(algo)
}