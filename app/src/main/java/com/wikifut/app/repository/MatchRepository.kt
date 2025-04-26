package com.wikifut.app.repository

import com.wikifut.app.api.MatchApi
import com.wikifut.app.model.MatchResponse

class MatchRepository(private val api: MatchApi) {

    suspend fun getMatchById(matchId: Long): MatchResponse? {
        val response = api.getInfoPartido(matchId)
        return response.response.firstOrNull()
    }
}