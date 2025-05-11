package com.wikifut.app.presentation.Ligas

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikifut.app.model.StandingTeam
import com.wikifut.app.repository.LigaDetalleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.wikifut.app.api.LigaDetalleApi
import com.wikifut.app.api.SeasonApi
import com.wikifut.app.model.TeamBasicInfo
import com.wikifut.app.repository.PartidosRepository
import com.wikifut.app.model.Partido
import com.wikifut.app.model.TopScorerItem

@HiltViewModel
class LigaDetalleViewModel @Inject constructor(
    private val repository: LigaDetalleRepository
): ViewModel() {

    @Inject lateinit var seasonApi: SeasonApi
    @Inject lateinit var ligaDetalleApi: LigaDetalleApi
    @Inject lateinit var partidosRepository: PartidosRepository


    private val _nombreLiga = mutableStateOf("")
    val nombreLiga: State<String> = _nombreLiga

    private val _tabla = mutableStateOf<List<StandingTeam>>(emptyList())
    val tabla: State<List<StandingTeam>> = _tabla

    private val _temporada = mutableStateOf(0)
    val temporada: State<Int> = _temporada

    private val _temporadasDisponibles = mutableStateOf<List<Int>>(emptyList())
    val temporadasDisponibles: State<List<Int>> = _temporadasDisponibles

    private val _equipos = mutableStateOf<List<TeamBasicInfo>>(emptyList())
    val equipos: State<List<TeamBasicInfo>> = _equipos

    private val _partidos = mutableStateOf<List<Partido>>(emptyList())
    val partidos: State<List<Partido>> = _partidos

    private val _topScorers = mutableStateOf<List<TopScorerItem>>(emptyList())
    val topScorers: State<List<TopScorerItem>> = _topScorers


    fun cargarTabla(leagueId: Int, season: Int) {
        viewModelScope.launch {
            val result = repository.getStandings(leagueId, season)
            result?.response?.firstOrNull()?.league?.standings?.firstOrNull()?.let { posiciones ->
                _tabla.value = posiciones
                _nombreLiga.value = result.response.first().league.name
                _temporada.value = season
            }
        }
    }

    fun obtenerTemporadaActual(leagueId: Int) {
        viewModelScope.launch {
            val result = ligaDetalleApi.getLigaDetalle(leagueId)
            if (result.isSuccessful) {
                val seasons = result.body()?.response?.firstOrNull()?.seasons ?: emptyList()
                _temporadasDisponibles.value = seasons.map { it.year }.sortedDescending()  // <-- todas ordenadas
                val temporadaActual = seasons.find { it.current }?.year ?: seasons.maxOfOrNull { it.year } ?: 0
                _temporada.value = temporadaActual
            }
        }
    }

    fun cargarEquipos(leagueId: Int, season: Int) {
        viewModelScope.launch {
            val result = ligaDetalleApi.getTeamsByLeagueAndSeason(leagueId, season)
            Log.d("LigaDetalle", "Llamando a cargarEquipos con leagueId=$leagueId, season=$season")
            if (result.isSuccessful) {
                _equipos.value = result.body()?.response?.map { it.team } ?: emptyList()
                Log.d("LigaDetalle", "Respuesta exitosa: ${result.body()}")
            } else {
                Log.e("Equipos", "Error: ${result.errorBody()?.string()}")
            }
        }
    }

    fun cargarPartidosPorLigaYTemporada(leagueId: Int, season: Int) {
        viewModelScope.launch {
            try {
                Log.d("LigaDetalle", "📆 Buscando partidos para liga=$leagueId, temporada=$season")
                val resultado = partidosRepository.getPartidosPorLigaYTemporada(leagueId, season)
                _partidos.value = resultado.response
                Log.d("LigaDetalle", "✅ Partidos encontrados: ${resultado.response.size}")
            } catch (e: Exception) {
                Log.e("LigaDetalle", "❌ Error cargando partidos: ${e.message}")
            }
        }
    }

    fun cargarTopScorers(leagueId: Int, season: Int) {
        viewModelScope.launch {
            try {
                val result = ligaDetalleApi.getTopScorers(leagueId, season)
                if (result.isSuccessful) {
                    _topScorers.value = result.body()?.response ?: emptyList()
                    Log.d("LigaDetalle", "✅ Goleadores cargados: ${_topScorers.value.size}")
                } else {
                    Log.e("LigaDetalle", "❌ Error en top scorers: ${result.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("LigaDetalle", "⚠️ Excepción en top scorers: ${e.message}")
            }
        }
    }
}
