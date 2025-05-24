package com.wikifut.app.model

data class ListaPartidos(
    val response: List<Partido>
)

data class ListaEquipos(
    val response: List<EquipoInfo>
)

data class EquipoInfo(
    val team: Team,
    val venue: Venue
)

data class ListaLigas(
    val response: List<LigaResponse>
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

data class Status(
    val long: String,
    val short: String,
    val elapsed: Int?
)

data class LigaResponse(
    val league: League,
    val country: CountryInfo,
    val seasons: List<Season>
)

data class League(
    val id: Int = 0,
    val name: String = "",
    val country: String = "",
    val logo: String = "",
    val flag: String? = null,
    val season: Int = 0,
    val round: String = ""
)

data class Season(
    val year: Int,
    val start: String,
    val end: String,
    val current: Boolean,
    val coverage: Coverage
)

data class Coverage(
    val fixtures: FixtureCoverage,
    val standings: Boolean,
    val players: Boolean,
    val top_scorers: Boolean,
    val top_assists: Boolean,
    val top_cards: Boolean,
    val injuries: Boolean,
    val predictions: Boolean,
    val odds: Boolean
)

data class FixtureCoverage(
    val events: Boolean,
    val lineups: Boolean,
    val statistics_fixtures: Boolean,
    val statistics_players: Boolean
)

data class Teams(
    val home: Team,
    val away: Team
)
data class FavoriteTeam(
    val team: Team = Team(),
    val venue: Venue = Venue()
)

data class Team(
    val id: Int = 0,
    val name: String = "",
    val logo: String = "",
    val winner: Boolean? = null,
    val country: String? = null
)

data class Venue(
    val id: Int? = 0,
    val name: String? = "",
    val address: String? = "",
    val city: String? = "",
    val capacity: Int? = 0,
    val surface: String? = "",
    val image: String? = ""
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
// estadisticas equipos
data class TeamStatisticsResponse(
    val response: TeamStatsResponse
)

data class TeamStatsResponse(
    val league: League,
    val team: Team,
    val fixtures: Fixtures,
    val goals: GoalsStats
)

//data class League(val id: Int, val name: String, val logo: String)
//data class Team(val id: Int, val name: String, val logo: String)
data class Fixtures(val played: FixtureTotal, val wins: FixtureTotal, val draws: FixtureTotal, val loses: FixtureTotal)
data class FixtureTotal(val home: Int, val away: Int, val total: Int)
data class GoalsStats(val `for`: GoalStats, val against: GoalStats)
data class GoalStats(val total: FixtureTotal, val average: GoalAverage)
data class GoalAverage(val home: String, val away: String, val total: String)
