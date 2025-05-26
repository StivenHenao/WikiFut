package com.wikifut.app.presentation.Favoritos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wikifut.app.R
import com.wikifut.app.model.League
import com.wikifut.app.model.Team
import com.wikifut.app.model.Venue
import com.wikifut.app.presentation.Header.Header
import com.wikifut.app.presentation.Search.LeagueItem
import com.wikifut.app.presentation.Search.PlayerItem
import com.wikifut.app.presentation.Search.TeamItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    onTeamClick: (Team, Venue) -> Unit,
    onLeagueClick: (League, Int) -> Unit,
    onPlayerClick: (String,String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val equipos by viewModel.favoritos_equipo.collectAsState()
    val ligas by viewModel.favoritos_liga.collectAsState()
    val players by viewModel.favoritos_player.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.cargarEquipoFavoritos()
        viewModel.cargarLigaFavoritos()
        viewModel.cargarPlayerFavoritos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(56.dp),
                title = { Text("Favoritos") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painterResource(R.drawable.ic_back),
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1F1235),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2D1B45))
                .padding(paddingValues)
        ) {
            Header(
                searchQuery = "",
                onSearchChange = {},
                onBuscar = { _, _ -> },
                actions = {}

            )

            Text(
                text = "Tus equipos favoritos",
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            if (equipos.isEmpty()) {
                Text(
                    text = "No tienes equipos favoritos",
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn() {
                    items(equipos) { favoriteTeam ->
                        TeamItem(
                            team = favoriteTeam.team,
                            onClick = { onTeamClick(favoriteTeam.team, favoriteTeam.venue) }
                        )
                    }
                }
            }

            Text(
                text = "Tus ligas favoritas",
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            if(ligas.isEmpty()){
                Text(
                    text = "No tienes Ligas favoritos",
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn() {
                    items(ligas) { favoriteLeague ->
                        LeagueItem(
                            league = favoriteLeague,
                            onClick = { onLeagueClick(favoriteLeague, favoriteLeague.season) }
                        )
                    }
                }

            }

            Text(
                text = "Tus jugadores favoritos",
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
            )

            if(players.isEmpty()) {
                Text(
                    text = "No tienes jugadores favoritos",
                    color = Color.White,
                    modifier = Modifier
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(players) { favoritePlayer ->
                        PlayerItem(
                            player = favoritePlayer,
                            onPlayerNavigate = { onPlayerClick(favoritePlayer.id.toString(), "2023") }
                        )
                    }
                }
            }
        }
    }
}
