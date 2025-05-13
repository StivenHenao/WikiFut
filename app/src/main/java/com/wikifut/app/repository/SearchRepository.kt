package com.wikifut.app.repository

import com.wikifut.app.api.PartidosApi
import com.wikifut.app.model.ListaEquipos
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val partidosApi: PartidosApi
){
    suspend fun buscarEquipo(query: String) : ListaEquipos {
        return partidosApi.buscarEquipos(query)
    }
}