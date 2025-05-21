package com.wikifut.app.presentation.Search

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.wikifut.app.model.Team
import com.wikifut.app.model.TipoBusqueda
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.wikifut.app.presentation.Header.Header
import com.wikifut.app.R
import com.wikifut.app.model.Venue
//import com.wikifut.app.presentation.Search.EquiposResult

val MoradoOscuro = Color(0xFF2E0854)    // Morado oscuro
val MoradoClaro = Color(0xFF7E57C2)    // Morado más claro para el Card

@Composable
fun SearchScreen(
    tipo: TipoBusqueda, // El tipo de búsqueda que quieres realizar
    query: String, // La consulta de búsqueda
    onSearchNavigate: (TipoBusqueda, String) -> Unit,
    HomeNavigate: () -> Unit,
    onTeamNavigate: (team: Team, venue: Venue) -> Unit,
    onPlayerNavigate: (playerId: String, season: String) -> Unit,
    onLigasNavigate: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()

) {
    // Estado para la búsqueda de texto
    var searchQuery by remember { mutableStateOf("") }

    // Disparar la búsqueda al entrar a la pantalla o si cambian el tipo o la consulta
    LaunchedEffect(key1 = tipo, key2 = query) {
        if (query.isNotEmpty()) {
            viewModel.buscar(tipo, query)
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MoradoOscuro
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            // barra de busqueda
            Header(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onBuscar = onSearchNavigate,
                actions = {
                    IconButton(onClick = { /* abrir filtro */ }) {
                        Icon(painterResource(R.drawable.ic_filter), contentDescription = "Filtrar", tint = Color.White)
                    }
                    IconButton(onClick = { HomeNavigate()  }) {
                        Icon(painterResource(R.drawable.ic_back), contentDescription = "Atrás", tint = Color.White)
                    }
                }
            )
            // Aquí solo mostramos el resultado que el usuario busco
            when (tipo) {
                TipoBusqueda.Equipos -> {
                    EquiposResult(viewModel,onTeamNavigate)
                }
                TipoBusqueda.Ligas -> {
                    LigasResult(viewModel)
                }
                TipoBusqueda.Jugadores -> {
                    PlayerResult(viewModel,onPlayerNavigate)
                }
                TipoBusqueda.Partidos -> TODO()
            }
        }
    }
}



