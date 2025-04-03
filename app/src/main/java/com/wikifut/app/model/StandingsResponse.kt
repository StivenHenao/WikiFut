package com.wikifut.app.model

data class StandingsResponse(
    val response: List<StandingsLeague>
)

data class StandingsLeague(
    val league: LeagueStandings
)

data class LeagueStandings(
    val standings: List<List<StandingTeam>>
)

data class StandingTeam(
    val rank: Int,
    val team: TeamData,
    val points: Int,
    val goalsDiff: Int,
    val all: AllStats
)

data class TeamData(
    val id: Int,
    val name: String,
    val logo: String
)

data class AllStats(
    val played: Int,
    val win: Int,
    val draw: Int,
    val lose: Int
)
