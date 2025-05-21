package com.wikifut.app.presentation.Favoritos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikifut.app.model.FavoriteTeam
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

    private val _favoritos = MutableStateFlow<List<FavoriteTeam>>(emptyList())
    val favoritos: StateFlow<List<FavoriteTeam>> = _favoritos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarFavoritos() {
        viewModelScope.launch {
            try {
                _favoritos.value = repository.obtenerFavoritos()
                Log.d("FavoritesViewModel", "Favoritos cargados: ${_favoritos.value.toString()}")
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Error al cargar favoritos", e)
                _error.value = "Error al cargar favoritos: ${e.message}"
            }
        }
    }

    fun addToFavorites(team: Team, venue: Venue) {
        val favoriteTeam = FavoriteTeam(team, venue)
        viewModelScope.launch {
            try {
                repository.agregarAFavoritos(favoriteTeam)
                cargarFavoritos()
            } catch (e: Exception) {
                _error.value = "Error al agregar a favoritos: ${e.message}"
            }
        }
    }
}

