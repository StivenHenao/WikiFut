package com.wikifut.app.presentation.Team

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wikifut.app.model.FavoriteTeam
import com.wikifut.app.model.TeamStatisticsResponse
import com.wikifut.app.repository.FavoritesRepository
import com.wikifut.app.repository.TeamsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val repository: TeamsRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _statsList = MutableStateFlow<List<TeamStatisticsResponse>>(emptyList())
    val statsList: StateFlow<List<TeamStatisticsResponse>> = _statsList

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _favoritos = MutableStateFlow<List<FavoriteTeam>>(emptyList())
    val favoritos: StateFlow<List<FavoriteTeam>> = _favoritos

    fun cargarFavoritos() {
        viewModelScope.launch {
            try {
                _favoritos.value = favoritesRepository.obtenerEquiposFavoritos()
                Log.d("FavoritesViewModel", "Favoritos cargados: ${_favoritos.value.toString()}")
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Error al cargar favoritos", e)
                _error.value = "Error al cargar favoritos: ${e.message}"
            }
        }
    }

    fun agregarAFavoritos(equipoFavorito: FavoriteTeam) {
        viewModelScope.launch {
            try {
                favoritesRepository.agregarEquipoAFavoritos(equipoFavorito)
                cargarFavoritos()
            } catch (e: Exception) {
                _error.value = "Error al agregar a favoritos: ${e.message}"
            }
        }
    }

    suspend fun isFavorite(teamId: Int): Boolean {
        val favoritos = favoritesRepository.obtenerEquiposFavoritos()
        return favoritos.any { it.team.id == teamId }
    }

    fun removeFromFavorites(teamId: Int) {
        viewModelScope.launch {
            try {
                favoritesRepository.eliminarEquipoFavorito(teamId)
                cargarFavoritos()
            } catch (e: Exception) {
                _error.value = "Error al eliminar favorito: ${e.message}"
            }
        }
    }

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
