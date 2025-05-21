package com.wikifut.app.presentation.Ligas


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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


@Composable
fun LigaDetalleScreen(
    leagueId: Int,
    season: Int,
    navController: NavHostController,
    viewModel: LigaDetalleViewModel = hiltViewModel()
) {
    // Estado para el tab seleccionado
    var selectedTab by remember { mutableStateOf(0) }

    val nombreLiga = viewModel.nombreLiga.value

    val temporada = viewModel.temporada.value
    val temporadas = viewModel.temporadasDisponibles.value

    var temporadaSeleccionada by remember { mutableStateOf(season) }
    var expanded by remember { mutableStateOf(false) }

    // Actualizar valores cuando cambia la temporada seleccionada

    LaunchedEffect(temporadaSeleccionada) {
        viewModel.cargarTabla(leagueId, temporadaSeleccionada)
        viewModel.cargarEquipos(leagueId, temporadaSeleccionada)
        viewModel.cargarTopScorers(leagueId, temporadaSeleccionada)
        viewModel.cargarPartidosPorLigaYTemporada(leagueId, temporadaSeleccionada)
        //viewModel.cargarStandings(leagueId, temporadaSeleccionada)
        viewModel.cargarAsistidores(leagueId, temporadaSeleccionada)
    }
    // Solo una vez
    LaunchedEffect(Unit) {
        viewModel.cargarTabla(leagueId, temporadaSeleccionada)
        viewModel.cargarEquipos(leagueId, temporadaSeleccionada)
        viewModel.cargarTopScorers(leagueId, temporadaSeleccionada)
        viewModel.cargarPartidosPorLigaYTemporada(leagueId, temporadaSeleccionada)
        //viewModel.cargarStandings(leagueId, temporadaSeleccionada)
        viewModel.cargarAsistidores(leagueId, temporadaSeleccionada)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Botón de regreso
        Text(
            text = "Atrás",
            modifier = Modifier
                .padding(12.dp)
                .clickable { navController.popBackStack() },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

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
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Informacion general", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Partidos", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("Clasificacion", modifier = Modifier.padding(12.dp))

            }
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }) {
                Text("Goleadores", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 4, onClick = { selectedTab = 4 }) {
                Text("Asistidores", modifier = Modifier.padding(12.dp))
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
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(partido.teams.home.name)
                    Text("${partido.goals?.home ?: "-"} - ${partido.goals?.away ?: "-"}")
                    Text(partido.teams.away.name)
                }
            }
        }
    }
}

@Composable
fun LigaInfoConEquiposTab(
    info: LeagueDetailItem, // tu modelo con los datos de la liga
    equipos: List<TeamBasicInfo> // lista de equipos
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
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                fontWeight = FontWeight.SemiBold
            )
        }

        // Equipos en formato de grillas
        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 1000.dp), // ajusta según el contenido
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false //  para evitar doble scroll
            ) {
                items(equipos) { equipo ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = equipo.logo,
                                contentDescription = equipo.name,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = equipo.name,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
