package com.wikifut.app.model

import com.google.gson.JsonElement

data class PlayerDataResponse(
    val get: String,
    val parameters: Parameters,
    val errors: Any?,
    val results: Int,
    val paging: Paging,
    val response: List<PlayerData>
)

data class Parameters(
    val id: Int,
    val team :Int?,
    val league: Int?,
    val season: Int?,
    val search: String?
)

data class Paging(
    val current: Int,
    val total: Int
)

data class PlayerData(
    val player: Player,
    val statistics: List<PlayerStatistic>
)

data class Player(
    val id: Int,
    val name: String,
    val firstname: String,
    val lastname: String,
    val age: Int,
    val birth: PlayerBirth,
    val nationality: String,
    val height: String,
    val weight: String,
    val injured: Boolean,
    val photo: String
)

data class PlayerBirth(
    val date: String,
    val place: String,
    val country: String
)

data class PlayerStatistic(
    val team: PlayerTeam,
    val league: PlayerLeague,
    val games: PlayerGames?,
    val substitutes: Substitutes?,
    val shots: Shots?,
    val goals: PlayerGoals?,
    val passes: Passes?,
    val tackles: Tackles?,
    val duels: Duels?,
    val dribbles: Dribbles?,
    val fouls: Fouls?,
    val cards: Cards?,
    val penalty: Penalty?
)

data class PlayerTeam(
    val id: Int,
    val name: String,
    val logo: String
)

data class PlayerLeague(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String?,
    val season: Int
)

data class PlayerGames(
    val appearences: Int?,
    val lineups: Int?,
    val minutes: Int?,
    val number: Int?,
    val position: String?,
    val rating: String?,
    val captain: Boolean?
)

data class Substitutes(
    val in_: Int?, // `in` es palabra reservada en Kotlin
    val out: Int?,
    val bench: Int?
)

data class Shots(
    val total: Int?,
    val on: Int?
)

data class PlayerGoals(
    val total: Int?,
    val conceded: Int?,
    val assists: Int?,
    val saves: Int?
)

data class Passes(
    val total: Int?,
    val key: Int?,
    val accuracy: Int?
)

data class Tackles(
    val total: Int?,
    val blocks: Int?,
    val interceptions: Int?
)

data class Duels(
    val total: Int?,
    val won: Int?
)

data class Dribbles(
    val attempts: Int?,
    val success: Int?,
    val past: Int?
)

data class Fouls(
    val drawn: Int?,
    val committed: Int?
)

data class Cards(
    val yellow: Int?,
    val yellowred: Int?,
    val red: Int?
)

data class Penalty(
    val won: Int?,
    val commited: Int?,
    val scored: Int?,
    val missed: Int?,
    val saved: Int?
)