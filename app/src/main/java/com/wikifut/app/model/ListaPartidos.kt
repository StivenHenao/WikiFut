package com.wikifut.app.model

data class ListaPartidos(
    val response: List<Partido>
)

data class Partido(
    val fixture: Fixture,
    val league: League,
    val teams: Teams,
    val goals: Goals?,
    val score: Score
)

data class Fixture(
    val id: Int,
    val timezone: String,
    val date: String,
    val timestamp: Long,
    val venue: Venue,
    val status: Status
)

data class Venue(
    val id: Int?,
    val name: String?,
    val city: String?
)

data class Status(
    val long: String,
    val short: String,
    val elapsed: Int?
)

data class League(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String?,
    val season: Int,
    val round: String
)

data class Teams(
    val home: Team,
    val away: Team
)

data class Team(
    val id: Int,
    val name: String,
    val logo: String,
    val winner: Boolean?
)

data class Goals(
    val home: Int?,
    val away: Int?
)

data class Score(
    val halftime: ScoreDetail?,
    val fulltime: ScoreDetail?,
    val extratime: ScoreDetail?,
    val penalty: ScoreDetail?
)

data class ScoreDetail(
    val home: Int?,
    val away: Int?
)
