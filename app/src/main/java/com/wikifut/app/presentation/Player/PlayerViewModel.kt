package com.wikifut.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wikifut.app.api.PlayerApi
import com.wikifut.app.model.League
import com.wikifut.app.model.Player
import com.wikifut.app.model.PlayerDataResponse
import com.wikifut.app.repository.FavoritesRepository
import com.wikifut.app.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _playerData = MutableLiveData<PlayerDataResponse?>() // Guarda los datos de la API
    val playerData: LiveData<PlayerDataResponse?> get() = _playerData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _favoritePlayersList = MutableStateFlow<List<Player>>(emptyList())
    val favoritePlayersList: StateFlow<List<Player>> = _favoritePlayersList


    fun fetchPlayerData(playerId: Int, season: Int) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val response = playerRepository.getPlayer(playerId, season)
                _playerData.value = response
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al obtener datos: ${e.message}"
            } finally {
                _loading.value = false // Se desactiva el loading
            }
        }
    }

    fun cargarJugadoresFavoritos() {
        viewModelScope.launch {
            try {
                val jugadoresFavoritos = favoritesRepository.obtenerJugadoresFavoritos()
                _favoritePlayersList.value = jugadoresFavoritos
            } catch (e: Exception) {
                _error.value = "Error al cargar jugadores favoritos: ${e.message}"
            }
        }
    }

    fun agregarJugadorAFavoritos(player: Player) {
        viewModelScope.launch {
            favoritesRepository.agregarJugadorAFavoritos(player)
        }
    }

    fun eliminarJugadorDeFavoritos(player: Player) {
        viewModelScope.launch {
            favoritesRepository.eliminarJugadorFavorito(player.id)
        }
    }
}
