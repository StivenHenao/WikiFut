package com.wikifut.app.model

data class LeagueDetailResponse(
    val response: List<LeagueDetailItem>
)

data class LeagueDetailItem(
    val league: LeagueDetailInfo,
    val country: CountryDetailInfo,
    val seasons: List<SeasonDetailInfo>
)

data class LeagueDetailInfo(
    val id: Int,
    val name: String,
    val type: String,
    val logo: String
)

data class CountryDetailInfo(
    val name: String,
    val code: String?,
    val flag: String?
)

data class SeasonDetailInfo(
    val year: Int,
    val start: String,
    val end: String,
    val current: Boolean
)
