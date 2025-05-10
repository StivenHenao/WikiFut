package com.wikifut.app.model

data class TopScorersResponse(
    val response: List<TopScorerItem>
)

data class TopScorerItem(
    val player: TopScorerPlayer,
    val statistics: List<TopScorerStatistics>
)

data class TopScorerPlayer(
    val id: Int,
    val name: String,
    val photo: String
)

data class TopScorerStatistics(
    val team: PlayerTeamInfo,
    val games: PlayerGames,
    val goals: PlayerGoals
)
