package com.wikifut.app.repository

import com.wikifut.app.api.PartidosApi
import com.wikifut.app.model.ListaPartidos
import com.wikifut.app.model.MatchResponse
import javax.inject.Inject

class MatchRepository @Inject constructor(
    private val api: PartidosApi
){
    suspend fun getMatchById(matchId: Long): MatchResponse? {
        val response = api.getInfoPartido(matchId)
        return response.response.firstOrNull()
    }
}
