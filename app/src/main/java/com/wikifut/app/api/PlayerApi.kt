package com.wikifut.app.api


import com.wikifut.app.model.PlayerDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayerApi {
    @GET("players/profiles")
    suspend fun getPlayer(
        @Query("player") player: Int? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int? = 1
    ): PlayerDataResponse
}