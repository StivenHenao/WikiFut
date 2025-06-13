package com.wikifut.app.presentation.Favoritos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikifut.app.model.FavoriteTeam
import com.wikifut.app.model.League
import com.wikifut.app.model.LigaResponse
import com.wikifut.app.model.Player
import com.wikifut.app.repository.FavoritesRepository
import com.wikifut.app.model.Team
import com.wikifut.app.model.Venue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepository
) : ViewModel() {

    private val _favoritos_equipo = MutableStateFlow<List<FavoriteTeam>>(emptyList())
    val favoritos_equipo: StateFlow<List<FavoriteTeam>> = _favoritos_equipo

    private val _favoritos_liga = MutableStateFlow<List<League>>(emptyList())
    val favoritos_liga: StateFlow<List<League>> = _favoritos_liga

    private val _favoritos_player = MutableStateFlow<List<Player>>(emptyList())
    val favoritos_player: StateFlow<List<Player>> = _favoritos_player

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarEquipoFavoritos() {
        viewModelScope.launch {
            try {
                _favoritos_equipo.value = repository.obtenerEquiposFavoritos()
                Log.d("FavoritesViewModel", "Favoritos cargados: ${_favoritos_equipo.value.toString()}")
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Error al cargar favoritos", e)
                _error.value = "Error al cargar favoritos: ${e.message}"
            }
        }
    }

    fun cargarLigaFavoritos(){
        viewModelScope.launch {
            try {
                _favoritos_liga.value = repository.obtenerLigasFavoritas()
                Log.d("FavoritesViewModel", "Favoritos cargados: ${_favoritos_liga.value.toString()}")
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Error al cargar favoritos", e)
                _error.value = "Error al cargar favoritos: ${e.message}"
            }
        }
    }

    fun cargarPlayerFavoritos() {
        viewModelScope.launch {
            try {
                _favoritos_player.value = repository.obtenerJugadoresFavoritos()
                Log.d("FavoritesViewModel", "Favoritos cargados: ${_favoritos_player.value.toString()}")
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Error al cargar favoritos", e)
                _error.value = "Error al cargar favoritos: ${e.message}"
            }
        }
    }
}

