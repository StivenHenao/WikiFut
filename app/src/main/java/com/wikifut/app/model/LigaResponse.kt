package com.wikifut.app.model

data class LigasResponse(
    val response: List<LigaData>
)

data class LigaData(
    val league: LigaInfo,
    val country: PaisInfo
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
