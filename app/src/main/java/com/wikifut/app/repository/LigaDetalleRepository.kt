package com.wikifut.app.repository

import com.wikifut.app.api.LigaDetalleApi
import com.wikifut.app.model.StandingsResponse
import javax.inject.Inject

class LigaDetalleRepository @Inject constructor(
    private val api: LigaDetalleApi
) {
    suspend fun getStandings(leagueId: Int, season: Int): StandingsResponse? {
        val response = api.getTablaPosiciones(leagueId, season)
        return if (response.isSuccessful) response.body() else null
    }
}
