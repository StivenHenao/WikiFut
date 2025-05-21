package com.wikifut.app.repository
import android.util.Log
import com.wikifut.app.api.LigaApi
import com.wikifut.app.model.LigaData
import javax.inject.Inject

class LigasRepository @Inject constructor(
    private val ligaApi: LigaApi
) {
    suspend fun obtenerLigas(): List<LigaData>? {
        Log.d("LigasRepository", "🔄 Iniciando llamada a getLigas()")
        val response = ligaApi.getLigas()
        Log.d("LigasRepository", "📥 Código de respuesta: ${response.code()}")
        return if (response.isSuccessful) {
            val ligas = response.body()?.response ?: emptyList()
            //response.body()?.response
            Log.d("LigasRepository", "✅ Ligas recibidas: ${ligas.size}")
            ligas
        } else {
            Log.e("LigasRepository", "❌ Error en la API: ${response.errorBody()?.string()}")
            null
        }
    }
}