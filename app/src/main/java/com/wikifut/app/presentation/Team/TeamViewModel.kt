package com.wikifut.app.presentation.Team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikifut.app.model.TeamStatisticsResponse
import com.wikifut.app.repository.TeamsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val repository: TeamsRepository
) : ViewModel() {

    private val _statsList = MutableStateFlow<List<TeamStatisticsResponse>>(emptyList())
    val statsList: StateFlow<List<TeamStatisticsResponse>> = _statsList

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarEstadisticasDeTodasLasLigas(teamId: Int, season: Int) {
        viewModelScope.launch {
            try {
                val ligasResponse = repository.getLeagues(teamId, season)

                val listaStats = mutableListOf<TeamStatisticsResponse>()

                ligasResponse?.response?.forEach { liga ->
                    try {
                        val stats = repository.getStatsForLeagues(
                            leagueId = liga.league.id,
                            teamId = teamId,
                            season = season
                        )
                        stats?.let { listaStats.add(it) }
                    } catch (e: Exception) {
                        _error.value = "Error con la liga ${liga.league.name}: ${e.message}"
                    }
                }

                _statsList.value = listaStats

            } catch (e: Exception) {
                _error.value = "Error al obtener ligas: ${e.message}"
            }
        }
    }
}
