package com.wikifut.app.model

data class TopAssistsResponse(
    val response: List<TopAssistItem>
)

data class TopAssistItem(
    val player: TopAssistPlayer,
    val statistics: List<TopAssistStatistics>
)

data class TopAssistPlayer(
    val id: Int,
    val name: String,
    val firstname: String?,
    val lastname: String?,
    val photo: String
)

data class TopAssistStatistics(
    val team: TopAssistTeam,
    val games: TopAssistGames,
    val goals: TopAssistGoals
)

data class TopAssistTeam(
    val id: Int,
    val name: String,
    val logo: String
)

data class TopAssistGames(
    val appearences: Int?,
    val minutes: Int?,
    val position: String?
)

data class TopAssistGoals(
    val total: Int?,
    val assists: Int?
)
