package com.wikifut.app.presentation.Favoritos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.wikifut.app.R

import com.wikifut.app.model.Team
import com.wikifut.app.model.Venue
import com.wikifut.app.presentation.Header.Header
import com.wikifut.app.presentation.Search.TeamItem

@Composable
fun FavoritosScreen(viewModel: FavoritesViewModel, onTeamClick: (Team, Venue) -> Unit, onBackClick: () -> Unit) {
    val equipos by viewModel.favoritos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarFavoritos()
    }

    if (equipos.isEmpty()) {
        Text("No tienes equipos favoritos", color = Color.White)
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = com.wikifut.app.presentation.Search.MoradoOscuro
        ) {
            var searchQuery = ""
            Header(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onBuscar = {  _, _ ->},
                actions = {
                    IconButton(onClick = { onBackClick()  }) {
                        Icon(painterResource(R.drawable.ic_back), contentDescription = "Atrás", tint = Color.White)
                    }
                }
            )
            LazyColumn {
                items(equipos) { favoriteTeam ->
                    TeamItem(team = favoriteTeam.team, onClick = { onTeamClick(favoriteTeam.team, favoriteTeam.venue) })
                }
            }
        }
    }
}
