package com.wikifut.app.model

class PlayerDataResponse(
    val get: String,
    val parameters: Parameters,
    val errors: List<Any>,
    val results: Int,
    val paging: Paging,
    val response: List<ResponseItem>
)

data class Parameters(
    val player: Int?, // ID del jugador (puede ser nulo si se usa búsqueda por apellido)
    val search: String?, // Apellido del jugador (mínimo 3 caracteres, puede ser nulo)
    val page: Int = 1 // Paginación, por defecto 1
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