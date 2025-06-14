package com.wikifut.app.presentation.Search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikifut.app.model.ListaEquipos
import com.wikifut.app.model.ListaLigas
import com.wikifut.app.model.ListaPartidos
import com.wikifut.app.model.ListaPlayers
import com.wikifut.app.model.TipoBusqueda
import com.wikifut.app.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _resultadoEquipos = MutableStateFlow<ListaEquipos?>(null)
    val resultadoEquipos: StateFlow<ListaEquipos?> = _resultadoEquipos

    private val _resultadoLigas = MutableStateFlow<ListaLigas?>(null)
    val resultadoLigas: StateFlow<ListaLigas?> = _resultadoLigas

    private val _resultadoPartidos = MutableStateFlow<ListaPartidos?>(null)
    val resultadoPartidos: StateFlow<ListaPartidos?> = _resultadoPartidos

    private val _resultadoJugadores = MutableStateFlow<ListaPlayers?>(null)
    val resultadoJugadores: StateFlow<ListaPlayers?> = _resultadoJugadores

    fun buscar(tipo: TipoBusqueda, query: String) {
        viewModelScope.launch {
            val queryTrimmed = query.trim()
            
            when(tipo) {
                TipoBusqueda.Equipos -> {
                    _resultadoEquipos.value = searchRepository.buscarEquipo(queryTrimmed)
                    _resultadoLigas.value = null
                    _resultadoPartidos.value = null
                }
                TipoBusqueda.Ligas -> {
                    _resultadoLigas.value = searchRepository.buscarLiga(queryTrimmed)
                    _resultadoEquipos.value = null
                    _resultadoPartidos.value = null
                }
                TipoBusqueda.Partidos -> {
                    //_resultadoPartidos.value = searchRepository.buscarPartido(queryTrimmed)
                    _resultadoEquipos.value = null
                    _resultadoLigas.value = null
                }
                TipoBusqueda.Jugadores -> {
                    _resultadoJugadores.value = searchRepository.buscarPlayer(queryTrimmed)
                    _resultadoEquipos.value = null
                    _resultadoLigas.value = null
                    _resultadoPartidos.value = null
                }
                else -> {
                    print("El tipo de búsqueda no es válido")
                    _resultadoEquipos.value = null
                    _resultadoLigas.value = null
                    _resultadoPartidos.value = null
                }
            }
        }
    }
}