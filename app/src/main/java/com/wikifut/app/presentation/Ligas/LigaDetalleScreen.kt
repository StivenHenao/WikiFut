package com.wikifut.app.presentation.Ligas

import android.util.Log
import androidx.compose.foundation.Image
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {

        // Header con botón de regreso
        CenterAlignedTopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Atrás",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
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
                tint = if (isFavorite) Color.Yellow else Color.White,
            )
        }

        // Nombre de liga y logo
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
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = rememberAsyncImagePainter("https://media.api-sports.io/football/leagues/$leagueId.png"),
                contentDescription = "Logo liga",
                modifier = Modifier.size(40.dp)
            )
        }

        // Menu desplegable con temporadas
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickable { expanded = true }
        ) {
            Text("Temporada: $temporadaSeleccionada")
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
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
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

        // Contenido según tab
        when (selectedTab) {
            0 -> {
                val info = viewModel.infoLiga.value
                val equipos = viewModel.equipos.value

                if (info != null) {
                    LigaInfoConEquiposTab(info = info, equipos = equipos)
                } else {
                    Text("Cargando información de la liga...", modifier = Modifier.padding(16.dp))
                }
            }
            1 -> {
                val partidos = viewModel.partidos.value
                if (partidos.isEmpty()) {
                    Text("No hay equipos disponibles de la temporada $temporadaSeleccionada", modifier = Modifier.padding(16.dp))
                } else {
                    ListaDePartidos(partidos)
                }
            }/*{  //Para mostrar la tabla de clasificacion completa
                val standings = viewModel.standings.value
                if (standings.isEmpty()) {
                    Text("No hay equipos disponibles de la temporada $temporadaSeleccionada", modifier = Modifier.padding(16.dp))
                } else {
                    TablaClasificacionCompleta(standings)
                }
            }*/
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

@Composable
fun TopAssistsTab(asistidores: List<TopAssistItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(asistidores) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = item.player.photo,
                            contentDescription = item.player.name,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(item.player.name, fontWeight = FontWeight.Bold)
                            Text("Equipo: ${item.statistics.firstOrNull()?.team?.name ?: "Desconocido"}")
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Asistencias: ${item.statistics.firstOrNull()?.goals?.assists ?: 0}")
                        Text("Posición: ${item.statistics.firstOrNull()?.games?.position ?: "?"}")
                    }
                }
            }
        }
    }
}

@Composable
fun TablaClasificacionCompleta(standings: List<StandingTeam>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(standings) { team ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${team.rank}. ${team.team.name}")
                    Text("Pts: ${team.points}")
                    Text("PJ: ${team.all.played}")
                    Text("G: ${team.all.win} E: ${team.all.draw} P: ${team.all.lose}")
                }
            }
        }
    }
}

@Composable
fun TablaGoleadores(goleadores: List<TopScorerItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(goleadores) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = item.player.photo,
                            contentDescription = item.player.name,
                            modifier = Modifier.size(40.dp).padding(end = 8.dp)
                        )
                        Column {
                            Text(text = item.player.name, fontWeight = FontWeight.Bold)
                            Text(text = item.statistics.firstOrNull()?.team?.name.orEmpty())
                        }
                    }
                    Text(
                        text = "${item.statistics.firstOrNull()?.goals?.total ?: 0} goles",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(partidos) { partido ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                            modifier = Modifier.padding(top = 4.dp)
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
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = partido.fixture.status.long,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                            modifier = Modifier.padding(top = 4.dp)
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
                AsyncImage(
                    model = info.league.logo,
                    contentDescription = "Logo Liga",
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = info.league.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
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
                    Text(text = info.country.name)
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
                textAlign = TextAlign.Center
            )
        }

        // Equipos en formato de grillas
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
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
