package com.wikifut.app.model

data class MatchResponse(
    val fixture: Fixture,
    val league: League,
    val teams: Teams,
    val goals: Goals,
    val score: Score,
    val events: List<Event>,
    val lineups: List<Lineup>,
    val statistics: List<TeamStatistics>,
    val players: List<TeamPlayers>
)

data class Fixture(
    val id: Long,
    val referee: String,
    val timezone: String,
    val date: String,
    val timestamp: Long,
    val periods: Periods,
    val venue: Venue,
    val status: Status
)

data class Periods(
    val first: Long,
    val second: Long
)

data class Status(
    val long: String,
    val short: String,
    val elapsed: Long,
    val extra: Any? = null
)

data class Venue(
    val id: Any? = null,
    val name: String,
    val city: String
)

data class League(
    val id: Long,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String,
    val season: Long,
    val round: String,
    val standings: Boolean
)

data class Teams(
    val home: Team,
    val away: Team
)

data class Goals(
    val home: Int?,
    val away: Int?
)

data class Team(
    val id: Long,
    val name: String,
    val logo: String,
    val colors: Any? = null,
    val update: String? = null,
    val winner: Boolean? = null
)

data class Event(
    val time: Time,
    val team: Team,
    val player: PlayerReference,
    val assist: PlayerReference,
    val type: EventType,
    val detail: String,
    val comments: Any? = null
)

data class Time(
    val elapsed: Long,
    val extra: Long? = null
)

enum class EventType {
    Goal, Card, Subst
}

data class PlayerReference(
    val id: Long? = null,
    val name: String? = null
)

data class Lineup(
    val team: Team,
    val coach: Coach,
    val formation: String,
    val startXI: List<StartingPlayer>,
    val substitutes: List<StartingPlayer>
)

data class Coach(
    val id: Long,
    val name: String,
    val photo: String
)

data class StartingPlayer(
    val player: PlayerLineupInfo
)

data class PlayerLineupInfo(
    val id: Long,
    val name: String,
    val number: Long,
    val pos: Position? = null,
    val grid: String? = null
)

enum class Position {
    G, D, M, F
}

data class TeamPlayers(
    val team: Team,
    val players: List<PlayerDetail>
)

data class PlayerDetail(
    val player: PlayerInfo,
    val statistics: List<PlayerStatistics>
)

data class PlayerInfo(
    val id: Long,
    val name: String,
    val photo: String
)

data class PlayerStatistics(
    val games: GameStats,
    val offsides: Long? = null,
    val shots: Shots,
    val goals: GoalStats,
    val passes: Passes,
    val tackles: Tackles,
    val duels: Duels,
    val dribbles: Dribbles,
    val fouls: Fouls,
    val cards: Cards,
    val penalty: Penalty
)

data class GameStats(
    val minutes: Long,
    val number: Long,
    val position: Position,
    val rating: String,
    val captain: Boolean,
    val substitute: Boolean
)

data class Shots(
    val total: Long,
    val on: Long
)

data class GoalStats(
    val total: Long? = null,
    val conceded: Long,
    val assists: Long? = null,
    val saves: Long? = null
)

data class Passes(
    val total: Long,
    val key: Long,
    val accuracy: String
)

data class Tackles(
    val total: Long? = null,
    val blocks: Long,
    val interceptions: Long
)

data class Duels(
    val total: Long,
    val won: Long
)

data class Dribbles(
    val attempts: Long,
    val success: Long,
    val past: Long? = null
)

data class Fouls(
    val drawn: Long,
    val committed: Long
)

data class Cards(
    val yellow: Long,
    val red: Long
)

data class Penalty(
    val won: Any? = null,
    val commited: Any? = null,
    val scored: Long,
    val missed: Long,
    val saved: Long? = null
)

data class Score(
    val halftime: Goals,
    val fulltime: Goals,
    val extratime: Goals,
    val penalty: Goals
)

data class TeamStatistics(
    val team: Team,
    val statistics: List<Statistic>
)

data class Statistic(
    val type: String,
    val value: StatValue
)

sealed class StatValue {
    class IntegerValue(val value: Long) : StatValue()
    class StringValue(val value: String) : StatValue()
    class NullValue : StatValue()
}