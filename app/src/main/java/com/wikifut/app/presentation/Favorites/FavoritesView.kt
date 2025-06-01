package com.wikifut.app.presentation.Favoritos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer

private val MoradoClaro = Color(0xFF4A256F)

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
                modifier = Modifier.height(70.dp),
                title = { 
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Favoritos",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_back),
                            contentDescription = "Atrás",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Fondo base
            Image(
                painter = painterResource(id = R.drawable.wikifutfondo1),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Box con blur que coincide con el header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(182.dp)
                    .graphicsLayer {
                        clip = true
                    }
                    .blur(radius = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wikifutfondo1),
                    contentDescription = "Blurred Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }

            // Sombra semitransparente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(182.dp)
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Box(modifier = Modifier.height(64.dp)) {
                    Header(
                        searchQuery = "",
                        onSearchChange = {},
                        onBuscar = { _, _ -> },
                        actions = {},
                        backgroundColor = Color.Transparent,
                        applyStatusBarPadding = false
                    )
                }

                var selectedTab by remember { mutableStateOf(0) }
                val tabs = listOf("Equipos", "Ligas", "Jugadores")

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    modifier = Modifier.height(48.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    color = if (selectedTab == index) Color.White else Color.Gray
                                )
                            }
                        )
                    }
                }

                when (selectedTab) {
                    0 -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            if (equipos.isEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = MoradoClaro),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Text(
                                            text = "No tienes equipos favoritos",
                                            color = Color.White,
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            } else {
                                items(equipos) { favoriteTeam ->
                                    TeamItem(
                                        team = favoriteTeam.team,
                                        onClick = { onTeamClick(favoriteTeam.team, favoriteTeam.venue) }
                                    )
                                }
                            }
                        }
                    }
                    1 -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            if(ligas.isEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = MoradoClaro),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Text(
                                            text = "No tienes ligas favoritas",
                                            color = Color.White,
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            } else {
                                items(ligas) { favoriteLeague ->
                                    LeagueItem(
                                        league = favoriteLeague,
                                        onClick = { onLeagueClick(favoriteLeague, favoriteLeague.season) }
                                    )
                                }
                            }
                        }
                    }
                    2 -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            if(players.isEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = MoradoClaro),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Text(
                                            text = "No tienes jugadores favoritos",
                                            color = Color.White,
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            } else {
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
        }
    }
}
