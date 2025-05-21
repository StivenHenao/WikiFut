package com.wikifut.app.model

data class LigaDetalleResponse(
    val response: List<LigaDetalle>
)

data class LigaDetalle(
    val league: LeagueInfo,
    val country: CountryInfo,
    val seasons: List<SeasonInfo>
)

data class LeagueInfo(
    val id: Int,
    val name: String,
    val logo: String
)

data class CountryInfo(
    val name: String,
    val flag: String
)

data class SeasonInfo(
    val year: Int,
    val current: Boolean
)
