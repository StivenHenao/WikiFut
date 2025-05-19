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
import androidx.compose.ui.unit.dp
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = com.wikifut.app.presentation.Search.MoradoOscuro
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                searchQuery = "",
                onSearchChange = {},
                onBuscar = { _, _ -> },
                actions = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            painterResource(R.drawable.ic_back),
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                }
            )

            Text(
                text = "Tus equipos favoritos",
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (equipos.isEmpty()) {
                Text(
                    text = "No tienes equipos favoritos",
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(equipos) { favoriteTeam ->
                        TeamItem(
                            team = favoriteTeam.team,
                            onClick = { onTeamClick(favoriteTeam.team, favoriteTeam.venue) }
                        )
                    }
                }
            }
        }
    }
}
