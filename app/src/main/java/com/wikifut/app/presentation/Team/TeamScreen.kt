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
import com.wikifut.app.model.FavoriteTeam
import kotlinx.coroutines.launch


@Composable
fun TeamScreen(
    team: Team,
    venue: Venue,
    onBackClick: () -> Unit,
    viewModel: TeamViewModel = hiltViewModel()
) {
    //val anioActual: Int = Calendar.getInstance().get(Calendar.YEAR)
    // limitacion de la api solo se pueden consultar datos entre 2021 y 2023 en el plan gratis papu sad :C
    val anioActual = 2023
    LaunchedEffect(Unit) {
        //Log.d("TeamScreen", "teamId: ${team.id}, season: $anioActual")
        viewModel.cargarEstadisticasDeTodasLasLigas(team.id,anioActual)
    }

    val resultadoState by viewModel.statsList.collectAsState()
    val resultado = resultadoState ?: emptyList()
    //var isFavorite by remember { mutableStateOf(false) }

    val favoritos by viewModel.favoritos.collectAsState()
    val isFavorite = favoritos.any { it.team.id == team.id }

    LaunchedEffect(Unit) {
        viewModel.cargarFavoritos()
    }

    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1D0B27))
    ) {
        // Sección con imagen de fondo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp) // Altura del fondo
        ) {
            // Imagen de fondo
            Image(
                painter = painterResource(id = R.drawable.bg_team_header), // Asegúrate de tener esta imagen
                contentDescription = "Fondo del equipo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Contenido sobre la imagen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Barra superior con botón de atrás y acciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_24),
                            contentDescription = "Atrás",
                            tint = Color.White
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
                        IconButton(onClick = { /* Acción de notificaciones */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_notifications_24),
                                contentDescription = "Notificaciones",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Logo y nombre del equipo centrado
                Row(
                    verticalAlignment = Alignment.CenterVertically
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
        }
        // vista estadio y estadisticas
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
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.Gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun VenueCard(venue: Venue) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            //.background(Color(0xFFE3B505), shape = RoundedCornerShape(20.dp))
            .background(Color(0xFFE3B505))
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(venue.image),
            contentDescription = "Imagen del estadio",
            modifier = Modifier
                .size(120.dp)
                //.clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)),
            //contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = venue.name ?: "Nombre desconocido",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            //Text(text = "Fundación: 1878") // puedes cambiar esto si tienes fecha
            Text(text = "Ciudad: ${venue.city ?: "-"}")
            Text(text = "Dirección: ${venue.address ?: "-"}")
            Text(text = "Capacidad: ${venue.capacity?.toString() ?: "-"}")
        }
    }
}


@Composable
fun LeagueStatsCardModern(stats: TeamStatsResponse) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD700)),
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
                Text(text = stats.league.name, style = MaterialTheme.typography.titleLarge)
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
                    textAlign = TextAlign.Center
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
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .padding(4.dp),
                    textAlign = TextAlign.Center
                )
                values.forEach {
                    Text(
                        text = it.toString(),
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp))
                            .padding(4.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


