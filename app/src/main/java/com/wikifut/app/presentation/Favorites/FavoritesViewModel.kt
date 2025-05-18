package com.wikifut.app.presentation.Favoritos

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

    fun cargarFavoritos() {
        viewModelScope.launch {
            _favoritos.value = repository.obtenerFavoritos()
        }
    }
    fun isFavorite(teamId: Int): Boolean {
        return _favoritos.value.any { it.team.id == teamId }
    }

    fun addToFavorites(team: Team, venue: Venue) {
        val favoriteTeam = FavoriteTeam (team, venue)
        viewModelScope.launch {
            repository.agregarAFavoritos(favoriteTeam)
            cargarFavoritos()
        }
    }

    fun remove_from_favorites(teamId: Int) {
        viewModelScope.launch {
            repository.eliminarFavorito(teamId)
            cargarFavoritos()
        }
    }
}
