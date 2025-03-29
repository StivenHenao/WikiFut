package com.wikifut.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wikifut.app.api.PlayerApi
import com.wikifut.app.model.PlayerDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerApi: PlayerApi
) : ViewModel() {

    private val _playerData = MutableLiveData<PlayerDataResponse?>() // Guarda los datos de la API
    val playerData: LiveData<PlayerDataResponse?> get() = _playerData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error


    fun fetchPlayerData(playerId: Int, season: Int) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val response = playerApi.getPlayer(playerId, season) // Llamada a la API
                _playerData.value = response
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al obtener datos: ${e.message}"
            } finally {
                _loading.value = false // Se desactiva el loading
            }
        }
    }
}
