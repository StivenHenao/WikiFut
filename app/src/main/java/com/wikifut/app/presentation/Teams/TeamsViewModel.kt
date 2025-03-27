package com.wikifut.app.presentation.Teams

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Equipo(val nombre: String, val logoUrl: String)

class TeamsViewModel : ViewModel() {
    private val _equipos = MutableStateFlow(
        listOf(
            Equipo("Real Madrid", "https://upload.wikimedia.org/wikipedia/en/5/56/Real_Madrid_CF.svg"),
            Equipo("FC Barcelona", "https://upload.wikimedia.org/wikipedia/en/4/47/FC_Barcelona_%28crest%29.svg"),
            Equipo("Manchester United", "https://upload.wikimedia.org/wikipedia/en/7/7a/Manchester_United_FC_crest.svg")
        )
    )

    val equipos: StateFlow<List<Equipo>> = _equipos
}