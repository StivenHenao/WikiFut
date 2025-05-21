package com.wikifut.app.repository

import com.wikifut.app.api.PlayerApi
import com.wikifut.app.model.PlayerDataResponse
import javax.inject.Inject

class PlayerRepository @Inject constructor(
    private val playerApi: PlayerApi
) {

    suspend fun getPlayer(playerId: Int, season: Int): PlayerDataResponse {
        return playerApi.getPlayer(player = playerId, season = season)
    }
}