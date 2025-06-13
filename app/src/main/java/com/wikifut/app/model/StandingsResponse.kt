package com.wikifut.app.model

data class StandingsResponse(
    val response: List<StandingsLeague>
)

data class StandingsLeague(
    val league: LeagueStandings
)

data class LeagueStandings(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String?,
    val season: Int,
    val standings: List<List<StandingTeam>>
)

data class StandingTeam(
    val rank: Int,
    val team: TeamInfo,
    val points: Int,
    val goalsDiff: Int,
    val group: String?,
    val form: String?,              // e.g. "WWDLW"
    val status: String?,           // e.g. "same", "up", "down"
    val description: String?,      // e.g. "Promotion - Champions League"
    val all: TeamStatsFull,
    val home: TeamStatsFull,
    val away: TeamStatsFull,
    val update: String             // e.g. "2023-05-28T00:00:00+00:00"
)

data class TeamInfo(
    val id: Int,
    val name: String,
    val logo: String
)

data class TeamStatsFull(
    val played: Int,
    val win: Int,
    val draw: Int,
    val lose: Int,
    val goals: StatingGoals
)

data class StatingGoals(
    val `for`: Int,
    val against: Int
)
