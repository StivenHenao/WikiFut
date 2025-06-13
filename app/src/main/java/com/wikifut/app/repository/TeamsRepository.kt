package com.wikifut.app.repository

import com.google.android.gms.common.api.Response
import com.wikifut.app.api.PartidosApi
import com.wikifut.app.model.ListaLigas
import com.wikifut.app.model.ListaPartidos
import com.wikifut.app.model.TeamStatisticsResponse
import javax.inject.Inject

class TeamsRepository @Inject constructor(
    private val partidosApi: PartidosApi
){
    suspend fun getLeagues(teamid: Int, seasonid: Int) : ListaLigas {
        return partidosApi.obtenerLigasPorEquipoYTemporada(teamId = teamid, season = seasonid)
    }

    suspend fun getStatsForLeagues(leagueId: Int, teamId: Int, season: Int) : TeamStatisticsResponse {
        return partidosApi.getTeamStats(leagueId = leagueId,teamId = teamId, season = season)
    }
}