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
    val season: Int,
    val standings: List<List<StandingTeam>>
)

data class StandingTeam(
    val rank: Int,
    val team: TeamInfo,
    val points: Int,
    val goalsDiff: Int,
    val all: TeamStats
)

data class TeamInfo(
    val id: Int,
    val name: String,
    val logo: String
)

data class TeamStats(
    val played: Int,
    val win: Int,
    val draw: Int,
    val lose: Int
)