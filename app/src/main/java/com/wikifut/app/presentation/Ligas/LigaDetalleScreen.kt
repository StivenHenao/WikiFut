package com.wikifut.app.presentation.Ligas

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.wikifut.app.model.StandingTeam
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.compose.AsyncImage
import com.wikifut.app.presentation.Ligas.LigaDetalleViewModel
import com.wikifut.app.presentation.Ligas.StandingsWidget
import com.wikifut.app.model.Partido
import com.wikifut.app.model.TopScorerItem
import com.wikifut.app.model.LeagueDetailItem
import com.wikifut.app.model.TeamBasicInfo
import com.wikifut.app.model.TopAssistItem
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import com.wikifut.app.model.FavoriteTeam
import com.wikifut.app.model.League
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import com.wikifut.app.model.LigaResponse
import androidx.compose.ui.res.painterResource
import com.wikifut.app.R
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LigaDetalleScreen(
    league: League,
    season: Int,
    navController: NavHostController,
    viewModel: LigaDetalleViewModel = hiltViewModel()
) {
    // Estado para el tab seleccionado
    val leagueId = league.id
    var selectedTab by remember { mutableStateOf(0) }

    val nombreLiga = viewModel.nombreLiga.value
    val temporada = viewModel.temporada.value
    val temporadas = viewModel.temporadasDisponibles.value
    var temporadaSeleccionada by remember { mutableStateOf(season) }
    var expanded by remember { mutableStateOf(false) }
    
    // Estado para el parpadeo del selector de temporada
    var hasBeenClicked by remember { mutableStateOf(false) }
    
    // Animación de parpadeo usando infiniteTransition
    val infiniteTransition = rememberInfiniteTransition(label = "blink")
    val blinkAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink"
    )
    
    // Solo parpadear si no se ha hecho clic
    val finalBlinkValue = if (hasBeenClicked) 0f else blinkAnimation

    // variables para favoritos
    val favoritos by viewModel.favoritos.collectAsState()
    val isFavorite = favoritos.any { it.id == leagueId }
    val coroutineScope = rememberCoroutineScope()

    // Actualizar valores cuando cambia la temporada seleccionada
    LaunchedEffect(temporadaSeleccionada) {
        viewModel.cargarTabla(leagueId, temporadaSeleccionada)
        viewModel.cargarEquipos(leagueId, temporadaSeleccionada)
        viewModel.cargarTopScorers(leagueId, temporadaSeleccionada)
        viewModel.cargarPartidosPorLigaYTemporada(leagueId, temporadaSeleccionada)
        viewModel.cargarAsistidores(leagueId, temporadaSeleccionada)
        viewModel.cargarInfoLiga(leagueId)
    }

    // Solo una vez
    LaunchedEffect(Unit) {
        viewModel.obtenerTemporadaActual(leagueId)
        viewModel.cargarTabla(leagueId, temporadaSeleccionada)
        viewModel.cargarEquipos(leagueId, temporadaSeleccionada)
        viewModel.cargarTopScorers(leagueId, temporadaSeleccionada)
        viewModel.cargarPartidosPorLigaYTemporada(leagueId, temporadaSeleccionada)
        viewModel.cargarAsistidores(leagueId, temporadaSeleccionada)
        viewModel.cargarInfoLiga(leagueId)
        viewModel.cargarFavoritos()
    }

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
                .height(224.dp)
                .graphicsLayer {
                    clip = true
                }
                .blur(radius = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.wikifutfondo1),
                contentDescription = "Background Blur",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }

        // Sombra semitransparente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(224.dp)
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Barra de notificaciones y TopAppBar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                // Header con botón de regreso
                CenterAlignedTopAppBar(
                    title = { },
                    navigationIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { navController.popBackStack() }
                                .padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Atrás",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Búsqueda",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                if (isFavorite) {
                                    Log.d("TeamScreen", "Se elimino el favorito")
                                    coroutineScope.launch {
                                        viewModel.removeFromFavorites(leagueId)
                                    }
                                } else {
                                    coroutineScope.launch {
                                        viewModel.agregarLigaAFavoritos(league)
                                    }
                                    Log.d("TeamScreen", "Se agrego el favorito teamId: ${leagueId}")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "Favorito",
                                tint = if (isFavorite) Color.Yellow else Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }

            // Nombre de liga y logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = nombreLiga.ifEmpty { "Liga" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Image(
                        painter = rememberAsyncImagePainter("https://media.api-sports.io/football/leagues/$leagueId.png"),
                        contentDescription = "Logo liga",
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.9f), shape = MaterialTheme.shapes.small)
                            .padding(4.dp)
                    )
                }
            }

            // Menu desplegable con temporadas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 2.dp, bottom = 4.dp)
            ) {
                Text(
                    "Temporada: $temporadaSeleccionada",
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 0.dp, end = 28.dp, top = 4.dp, bottom = 4.dp)
                        .background(
                            color = Color.White.copy(alpha = finalBlinkValue * 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { 
                            expanded = true
                            hasBeenClicked = true
                        }
                )
                
                // Dropdown menu
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    temporadas.forEach { year ->
                        DropdownMenuItem(
                            text = { Text("Temporada $year") },
                            onClick = {
                                expanded = false
                                temporadaSeleccionada = year
                            }
                        )
                    }
                }
            }

            // Tabs: Informacion General | Partidos | Tabla | Goleadores | Asistidores
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    items(listOf(
                        "Información general" to 0,
                        "Partidos" to 1,
                        "Clasificación" to 2,
                        "Goleadores" to 3,
                        "Asistidores" to 4
                    )) { (text, index) ->
                        NavigationOption(
                            text = text,
                            isSelected = selectedTab == index,
                            onClick = { selectedTab = index }
                        )
                    }
                }
            }

            // Contenido principal
            when (selectedTab) {
                0 -> {
                    val info = viewModel.infoLiga.value
                    val equipos = viewModel.equipos.value

                    if (info != null) {
                        LigaInfoConEquiposTab(info = info, equipos = equipos)
                    } else {
                        Text(
                            "Cargando información de la liga...",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
                1 -> {
                    val partidos = viewModel.partidos.value
                    if (partidos.isEmpty()) {
                        Text(
                            "No hay equipos disponibles de la temporada $temporadaSeleccionada",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    } else {
                        ListaDePartidos(partidos)
                    }
                }
                2 -> {
                    StandingsWidget(
                        leagueId = leagueId,
                        season = temporada
                    )
                }
                3 -> {
                    val goleadores = viewModel.topScorers.value
                    if (goleadores.isEmpty()) {
                        Text("No hay goleadores disponibles de la temporada $temporadaSeleccionada", modifier = Modifier.padding(16.dp))
                    } else {
                        TablaGoleadores(goleadores)
                    }
                }
                4 -> {
                    val asistidores = viewModel.asistidores.value
                    if (asistidores.isEmpty()) {
                        Text("No hay asistidores disponibles de la temporada $temporadaSeleccionada", modifier = Modifier.padding(16.dp))
                    } else {
                        TopAssistsTab(asistidores)
                    }
                }

            }
        }
    }

}

@Composable
fun TopAssistsTab(asistidores: List<TopAssistItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(asistidores) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3B1E5E).copy(alpha = 0.9f)) // morado oscuro
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Foto + nombre
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = item.player.photo,
                            contentDescription = item.player.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = item.player.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Equipo: ${item.statistics.firstOrNull()?.team?.name ?: "Desconocido"}",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // Asistencias + Posición
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "🎯 ${item.statistics.firstOrNull()?.goals?.assists ?: 0} asistencias",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "📍 ${item.statistics.firstOrNull()?.games?.position ?: "?"}",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TablaGoleadores(goleadores: List<TopScorerItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(goleadores) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3B1E5E).copy(alpha = 0.9f)) // morado oscuro
            )  {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Foto + nombre + equipo
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = item.player.photo,
                            contentDescription = item.player.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Column {
                            Text(
                                text = item.player.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = item.statistics.firstOrNull()?.team?.name.orEmpty(),
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // Goles
                    Text(
                        text = "⚽ ${item.statistics.firstOrNull()?.goals?.total ?: 0}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun ListaDePartidos(partidos: List<Partido>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(partidos) { partido ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(7.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF3B1E5E).copy(alpha = 0.9f) // morado oscuro translúcido
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Equipo local
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        AsyncImage(
                            model = partido.teams.home.logo,
                            contentDescription = partido.teams.home.name,
                            modifier = Modifier.size(40.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = partido.teams.home.name,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            modifier = Modifier.padding(top = 4.dp),
                            color = Color.White
                        )
                    }

                    // Marcador
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "${partido.goals?.home ?: "-"} - ${partido.goals?.away ?: "-"}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = partido.fixture.status.long,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }

                    // Equipo visitante
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        AsyncImage(
                            model = partido.teams.away.logo,
                            contentDescription = partido.teams.away.name,
                            modifier = Modifier.size(40.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = partido.teams.away.name,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            modifier = Modifier.padding(top = 4.dp),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun LigaInfoConEquiposTab(
    info: LeagueDetailItem,
    equipos: List<TeamBasicInfo>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Info de la liga
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF9559DD).copy(alpha = 0.9f)),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(7.dp)
                ) {
                    AsyncImage(
                        model = info.league.logo,
                        contentDescription = "Logo Liga",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(12.dp)
                    )
                }

                Text(
                    text = info.league.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color.White
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val imageLoader = ImageLoader.Builder(LocalContext.current)
                        .components {
                            add(SvgDecoder.Factory())
                        }
                        .build()

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = info.country.flag,
                            imageLoader = imageLoader
                        ),
                        contentDescription = "Bandera país",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = info.country.name,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // Título de equipos
        item {
            Text(
                text = "Equipos participantes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

        // Equipos en grilla
        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 1000.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                items(equipos) { equipo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF3B1E5E).copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(
                                model = equipo.logo,
                                contentDescription = equipo.name,
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(bottom = 8.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = equipo.name,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                textAlign = TextAlign.Center,
                                color = Color.White, // importante para contraste
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun NavigationOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            color = if (isSelected) 
                MaterialTheme.colorScheme.onPrimary 
            else 
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
