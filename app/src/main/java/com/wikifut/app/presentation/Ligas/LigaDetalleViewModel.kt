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

@HiltViewModel
class LigaDetalleViewModel @Inject constructor(
    private val repository: LigaDetalleRepository
): ViewModel() {

    @Inject lateinit var seasonApi: SeasonApi
    @Inject lateinit var ligaDetalleApi: LigaDetalleApi

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
            if (result.isSuccessful) {
                _equipos.value = result.body()?.response?.map { it.team } ?: emptyList()
            } else {
                Log.e("Equipos", "Error: ${result.errorBody()?.string()}")
            }
        }
    }


}
