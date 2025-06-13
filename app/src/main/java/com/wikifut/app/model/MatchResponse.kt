package com.wikifut.app.model

data class MatchApiResponse(
    val response: List<MatchResponse>
)

data class MatchResponse(
    val fixture: MRFixture,
    val league: MRLeague,
    val teams: MRTeams,
    val goals: MRGoals,
    val score: MRScore,
    val events: List<MREvent>,
    val lineups: List<MRLineup>,
    val statistics: List<MRTeamStatistics>,
    val players: List<MRTeamPlayers>
)

data class MRFixture(
    val id: Long,
    val referee: String,
    val timezone: String,
    val date: String,
    val timestamp: Long,
    val periods: MRPeriods,
    val venue: MRVenue,
    val status: MRStatus
)

data class MRPeriods(
    val first: Long,
    val second: Long
)

data class MRStatus(
    val long: String,
    val short: String,
    val elapsed: Long,
    val extra: Any? = null
)

data class MRVenue(
    val id: Any? = null,
    val name: String,
    val city: String
)

data class MRLeague(
    val id: Long,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String,
    val season: Long,
    val round: String,
    val standings: Boolean
)

data class MRTeams(
    val home: MRTeam,
    val away: MRTeam
)

data class MRGoals(
    val home: Int?,
    val away: Int?
)

data class MRTeam(
    val id: Long,
    val name: String,
    val logo: String,
    val colors: Any? = null,
    val update: String? = null,
    val winner: Boolean? = null
)

data class MREvent(
    val time: MRTime,
    val team: MRTeam,
    val player: MRPlayerReference,
    val assist: MRPlayerReference,
    val type: MREventType,
    val detail: String,
    val comments: Any? = null
)

data class MRTime(
    val elapsed: Long,
    val extra: Long? = null
)

enum class MREventType {
    Goal, Card, Subst
}

data class MRPlayerReference(
    val id: Long? = null,
    val name: String? = null
)

data class MRLineup(
    val team: MRTeam,
    val coach: MRCoach,
    val formation: String,
    val startXI: List<MRStartingPlayer>,
    val substitutes: List<MRStartingPlayer>
)

data class MRCoach(
    val id: Long,
    val name: String,
    val photo: String
)

data class MRStartingPlayer(
    val player: MRPlayerLineupInfo
)

data class MRPlayerLineupInfo(
    val id: Long,
    val name: String,
    val number: Long,
    val pos: MRPosition? = null,
    val grid: String? = null
)

enum class MRPosition {
    G, D, M, F
}

data class MRTeamPlayers(
    val team: MRTeam,
    val players: List<MRPlayerDetail>
)

data class MRPlayerDetail(
    val player: MRPlayerInfo,
    val statistics: List<MRPlayerStatistics>
)

data class MRPlayerInfo(
    val id: Long,
    val name: String,
    val photo: String
)

data class MRPlayerStatistics(
    val games: MRGameStats,
    val offsides: Long? = null,
    val shots: MRShots,
    val goals: MRGoalStats,
    val passes: MRPasses,
    val tackles: MRTackles,
    val duels: MRDuels,
    val dribbles: MRDribbles,
    val fouls: MRFouls,
    val cards: MRCards,
    val penalty: MRPenalty
)

data class MRGameStats(
    val minutes: Long,
    val number: Long,
    val position: MRPosition,
    val rating: String,
    val captain: Boolean,
    val substitute: Boolean
)

data class MRShots(
    val total: Long,
    val on: Long
)

data class MRGoalStats(
    val total: Long? = null,
    val conceded: Long,
    val assists: Long? = null,
    val saves: Long? = null
)

data class MRPasses(
    val total: Long,
    val key: Long,
    val accuracy: String
)

data class MRTackles(
    val total: Long? = null,
    val blocks: Long,
    val interceptions: Long
)

data class MRDuels(
    val total: Long,
    val won: Long
)

data class MRDribbles(
    val attempts: Long,
    val success: Long,
    val past: Long? = null
)

data class MRFouls(
    val drawn: Long,
    val committed: Long
)

data class MRCards(
    val yellow: Long,
    val red: Long
)

data class MRPenalty(
    val won: Any? = null,
    val commited: Any? = null,
    val scored: Long,
    val missed: Long,
    val saved: Long? = null
)

data class MRScore(
    val halftime: MRGoals,
    val fulltime: MRGoals,
    val extratime: MRGoals,
    val penalty: MRGoals
)

data class MRTeamStatistics(
    val team: MRTeam,
    val statistics: List<MRStatistic>
)

data class MRStatistic(
    val type: String,
    val value: String? // o Any? si estás usando Gson
)