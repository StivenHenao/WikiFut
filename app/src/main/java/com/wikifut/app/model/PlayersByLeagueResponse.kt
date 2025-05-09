package com.wikifut.app.model

data class PlayersByLeagueResponse(
    val response: List<PlayerItem>,
    val paging: Paging
)

data class PlayerItem(
    val player: PlayerSummary,
    val statistics: List<PlayerStatistics>
)

data class PlayerSummary(
    val id: Int,
    val name: String,
    val firstname: String?,
    val lastname: String?,
    val photo: String
)

data class PlayerStatistics(
    val team: PlayerTeamInfo,
    val games: PlayerGames,
    val goals: PlayerGoals,
    val cards: PlayerCards
)

data class PlayerTeamInfo(
    val id: Int,
    val name: String,
    val logo: String
)

data class PlayerGames(
    val appearences: Int?,
    val minutes: Int?,
    val position: String?
)

data class PlayerGoals(
    val total: Int?,
    val assists: Int?
)

data class PlayerCards(
    val yellow: Int?,
    val red: Int?
)
