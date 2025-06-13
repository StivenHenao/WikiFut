package com.wikifut.app.api
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import com.wikifut.app.model.LigasResponse

interface LigaApi {
    @GET("leagues")
    suspend fun getLigas(): Response<LigasResponse>
}