package com.wikifut.app.repository
import com.wikifut.app.api.LigaApi
import com.wikifut.app.model.LigaData
import javax.inject.Inject

class LigasRepository @Inject constructor(
    private val ligaApi: LigaApi
) {
    suspend fun obtenerLigas(): List<LigaData>? {
        val response = ligaApi.getLigas()
        return if (response.isSuccessful) response.body()?.response else null
    }
}