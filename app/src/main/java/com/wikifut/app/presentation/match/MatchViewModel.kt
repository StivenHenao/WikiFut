package com.wikifut.app.presentation.match

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikifut.app.model.MatchResponse
import com.wikifut.app.repository.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: MatchRepository
) : ViewModel() {

    private val _match = MutableStateFlow<MatchResponse?>(null)
    val match: StateFlow<MatchResponse?> = _match.asStateFlow()

    init {
        Log.i("wikifut", "MatchViewModel created");
        Log.i("Información de partidos","${match.value}")
        Log.i("Información de partidos","Pasada")
    }

    fun fetchMatch(matchId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.getMatchById(matchId)
                _match.value = response
                Log.i("wikifut", "Partido obtenido: ${response?.fixture?.id}")
            } catch (e: Exception) {
                Log.e("MatchViewModel", "Error al obtener el partido", e)
                _match.value = null
            }
        }
    }
}