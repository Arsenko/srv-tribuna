package com.tribuna.models

import kotlinx.serialization.Serializable

@Serializable
class CounterChangeDto(
    val counterType:CounterType,
    val id:Int,
    val increase:Boolean
)