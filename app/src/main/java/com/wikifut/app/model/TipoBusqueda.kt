package com.wikifut.app.model

sealed class TipoBusqueda(val nombre: String) {
    object Equipos : TipoBusqueda("Equipos")
    object Ligas : TipoBusqueda("Ligas")
    object Partidos : TipoBusqueda("Partidos")
    object Jugadores : TipoBusqueda("Jugadores")
}