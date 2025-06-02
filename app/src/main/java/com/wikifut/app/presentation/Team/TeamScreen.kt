package com.wikifut.app.presentation.Team

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.wikifut.app.R
import com.wikifut.app.model.Team
import com.wikifut.app.model.Venue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign
import com.wikifut.app.model.TeamStatsResponse
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import com.wikifut.app.model.FavoriteTeam
import kotlinx.coroutines.launch
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(
    team: Team,
    venue: Venue,
    onBackClick: () -> Unit,
    viewModel: TeamViewModel = hiltViewModel()
) {
    val anioActual = 2023
    LaunchedEffect(Unit) {
        viewModel.cargarEstadisticasDeTodasLasLigas(team.id,anioActual)
    }

    val resultadoState by viewModel.statsList.collectAsState()
    val resultado = resultadoState ?: emptyList()

    val favoritos by viewModel.favoritos.collectAsState()
    val isFavorite = favoritos.any { it.team.id == team.id }

    LaunchedEffect(Unit) {
        viewModel.cargarFavoritos()
    }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(70.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_back_24),
                                    contentDescription = "Volver",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(
                                text = "Detalles de Equipo",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        
                        Row {
                            IconButton(
                                onClick = {
                                    if (isFavorite) {
                                        Log.d("TeamScreen", "Se elimino el favorito")
                                        coroutineScope.launch {
                                            viewModel.removeFromFavorites(team.id)
                                        }
                                    } else {
                                        coroutineScope.launch {
                                            viewModel.agregarAFavoritos(
                                                FavoriteTeam(
                                                    team = team,
                                                    venue = venue
                                                )
                                            )
                                        }
                                        Log.d("TeamScreen", "Se agrego el favorito teamId: ${team.id}")
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                                    contentDescription = "Favorito",
                                    tint = if (isFavorite) Color.Yellow else Color.White,
                                )
                            }
                        }
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Fondo base
            Image(
                painter = painterResource(id = R.drawable.wikifutfondo1),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Box con blur
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .graphicsLayer {
                        clip = true
                    }
                    .blur(radius = 20.dp)
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
                    .height(170.dp)
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                // Box para el header con logo y nombre
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .background(Color.Transparent)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(team.logo),
                            contentDescription = "Logo de ${team.name}",
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.White, shape = CircleShape)
                                .padding(8.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.width(30.dp))
                        Column(
                            horizontalAlignment = Alignment.Start
                        ){
                            Text(
                                text = team.name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = team.country ?: "País no disponible",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                // Contenido scrolleable
                LazyColumn {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        TextoCentradoIcono(text = "Estadio", icon = R.drawable.sport)
                    }

                    item {
                        VenueCard(venue = venue)
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        TextoCentradoIcono(text = "Estadísticas", icon = R.drawable.analytics)
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    if (resultado.isEmpty()) {
                        item {
                            Text(
                                text = "No hay resultados de ligas",
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        items(resultado) { statsResponse ->
                            LeagueStatsCardModern(statsResponse.response)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextoCentradoIcono(text: String, icon: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Icono de estadio",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(2f, 2f),
                    blurRadius = 12f
                )
            )
        )
    }
}

@Composable
fun VenueCard(venue: Venue) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFFE3B505))
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(venue.image),
            contentDescription = "Imagen del estadio",
            modifier = Modifier
                .size(120.dp),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = venue.name ?: "Nombre desconocido",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Ciudad: ${venue.city ?: "-"}",
                color = Color.Black
            )
            Text(
                text = "Dirección: ${venue.address ?: "-"}",
                color = Color.Black
            )
            Text(
                text = "Capacidad: ${venue.capacity?.toString() ?: "-"}",
                color = Color.Black
            )
        }
    }
}


@Composable
fun LeagueStatsCardModern(stats: TeamStatsResponse) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3B505)),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = stats.league.logo,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stats.league.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
            }
            SectionTitle("⚽ Juegos")
            StatTable(
                headers = listOf("Casa", "Visitante", "Todo"),
                rows = listOf(
                    "Jugados" to listOf(stats.fixtures.played.home, stats.fixtures.played.away, stats.fixtures.played.total),
                    "Ganados" to listOf(stats.fixtures.wins.home, stats.fixtures.wins.away, stats.fixtures.wins.total),
                    "Empates" to listOf(stats.fixtures.draws.home, stats.fixtures.draws.away, stats.fixtures.draws.total),
                    "Perdidos" to listOf(stats.fixtures.loses.home, stats.fixtures.loses.away, stats.fixtures.loses.total)
                )
            )

            SectionTitle("⚽ Goles")
            StatTable(
                headers = listOf("Casa", "Visitante", "Todo"),
                rows = listOf(
                    "A favor" to listOf(stats.goals.`for`.total.home, stats.goals.`for`.total.away, stats.goals.`for`.total.total),
                    "En contra" to listOf(stats.goals.against.total.home, stats.goals.against.total.away, stats.goals.against.total.total)
                )
            )

            SectionTitle("⚽ Promedio del Gol")
            StatTable(
                headers = listOf("Casa", "Visitante", "Todo"),
                rows = listOf(
                    "Goles a favor" to listOf(
                        stats.goals.`for`.average.home,
                        stats.goals.`for`.average.away,
                        stats.goals.`for`.average.total
                    ),
                    "Goles en contra" to listOf(
                        stats.goals.against.average.home,
                        stats.goals.against.average.away,
                        stats.goals.against.average.total
                    )
                )
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        color = Color.Black
    )
}

@Composable
fun StatTable(headers: List<String>, rows: List<Pair<String, List<Any>>>) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Spacer(modifier = Modifier.weight(1f))
            headers.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        rows.forEach { (label, values) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp))
                        .padding(4.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
                values.forEach {
                    Text(
                        text = it.toString(),
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp))
                            .padding(4.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }
            }
        }
    }
}


