package com.wikifut.app.model

import java.security.Policy

class PlayerDataResponse(
    val get: String,
    val parameters: Policy.Parameters,
    val errors: List<Any>,
    val results: Int,
    val paging: Paging,
    val response: List<ResponseItem>
)

data class Parameters(
    val player: String
)

data class ListaPlayers(
    val response: List<PlayerInfo>
)

data class PlayerInfo(
    val player: Player
)

data class Paging(
    val current: Int,
    val total: Int
)

data class ResponseItem(
    val player: Player
)

data class Player(
    val id: Int,
    val name: String,
    val firstname: String,
    val lastname: String,
    val age: Int,
    val birth: Birth,
    val nationality: String,
    val height: String,
    val weight: String,
    val number: Int,
    val position: String,
    val photo: String
)

data class Birth(
    val date: String,
    val place: String,
    val country: String
)