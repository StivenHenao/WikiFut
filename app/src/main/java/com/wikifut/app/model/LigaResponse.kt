package com.wikifut.app.model

data class LigasResponse(
    val response: List<LigaData>
)

data class LigaData(
    val league: LeagueInfo,
    val country: CountryInfo,
    val seasons: List<SeasonInfo> = emptyList()
)

data class LigaInfo(
    val id: Int,
    val name: String,
    val logo: String,
    val season: Int
)

data class PaisInfo(
    val name: String,
    val flag: String?
)

