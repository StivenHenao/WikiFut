package com.wikifut.app.api

import com.wikifut.app.model.SeasonResponse
import retrofit2.Response
import retrofit2.http.GET

interface SeasonApi {
    @GET("leagues/seasons")
    suspend fun getSeasons(): Response<SeasonResponse>
}
