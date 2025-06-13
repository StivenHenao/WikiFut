package com.wikifut.app.model

data class TeamsResponse(
    val response: List<TeamItem>
)

data class TeamItem(
    val team: TeamBasicInfo
)

data class TeamBasicInfo(
    val id: Int,
    val name: String,
    val logo: String
)
data class VenueInfo(
    val name: String,
    val city: String,
    val capacity: Int,
    val image: String
)