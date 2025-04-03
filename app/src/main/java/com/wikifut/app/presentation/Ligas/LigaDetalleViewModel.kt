package com.wikifut.app.presentation.Ligas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikifut.app.model.StandingTeam
import com.wikifut.app.repository.LigaDetalleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

@HiltViewModel
class LigaDetalleViewModel @Inject constructor(
    private val repository: LigaDetalleRepository
): ViewModel() {

    private val _tabla = mutableStateOf<List<StandingTeam>>(emptyList())
    val tabla: State<List<StandingTeam>> = _tabla

    fun cargarTabla(leagueId: Int, season: Int) {
        viewModelScope.launch {
            val result = repository.getStandings(leagueId, season)
            result?.response?.firstOrNull()?.league?.standings?.firstOrNull()?.let {
                _tabla.value = it
            }
        }
    }
}
