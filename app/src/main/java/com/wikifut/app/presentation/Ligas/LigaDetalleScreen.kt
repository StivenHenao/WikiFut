package com.wikifut.app.presentation.LigaDetalle

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.wikifut.app.model.StandingTeam
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.wikifut.app.presentation.Ligas.LigaDetalleViewModel
import com.wikifut.app.presentation.Ligas.StandingsWidget
import com.wikifut.app.model.Partido
import com.wikifut.app.model.TopScorerItem
import com.wikifut.app.model.LeagueDetailItem


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

    // Llamar solo una vez
    // Llamar al cargar tabla cuando cambia la temporada seleccionada
    LaunchedEffect(temporadaSeleccionada) {
        viewModel.cargarTabla(leagueId, temporadaSeleccionada)
    }
    LaunchedEffect(temporadaSeleccionada) {
        viewModel.cargarEquipos(leagueId, temporadaSeleccionada)
    }

    LaunchedEffect(temporadaSeleccionada) {
        viewModel.cargarTopScorers(leagueId, temporadaSeleccionada)
        viewModel.cargarPartidosPorLigaYTemporada(leagueId, temporadaSeleccionada)
        viewModel.cargarStandings(leagueId, temporadaSeleccionada)
    }

    // Solo una vez
    LaunchedEffect(Unit) {
        viewModel.obtenerTemporadaActual(leagueId)
        viewModel.cargarTabla(leagueId, temporadaSeleccionada)
        viewModel.cargarEquipos(leagueId, temporadaSeleccionada)
        //viewModel.cargarStandings(leagueId, temporadaSeleccionada)
        viewModel.cargarInfoLiga(leagueId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 🔙 Botón de regreso
        Text(
            text = "Atrás",
            modifier = Modifier
                .padding(12.dp)
                .clickable { navController.popBackStack() },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // 🏆 Nombre de liga y logo
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

        // 🧭 Tabs: Tabla | Equipos | Temporada
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Partidos", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Clasificacion", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("Equipos", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }) {
                Text("Goleadores", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 5, onClick = { selectedTab = 5 }) {
                Text("Info", modifier = Modifier.padding(12.dp))
            }
        }

        // 📋 Contenido según tab
        when (selectedTab) {
            0 -> {
                val partidos = viewModel.partidos.value
                if (partidos.isEmpty()) {
                    Text("No hay equipos disponibles de la temporada $temporadaSeleccionada", modifier = Modifier.padding(16.dp))
                } else {
                    ListaDePartidos(partidos)
                }
            }
            1 -> /*{
                val standings = viewModel.standings.value
                if (standings.isEmpty()) {
                    Text("No hay equipos disponibles de la temporada $temporadaSeleccionada", modifier = Modifier.padding(16.dp))
                } else {
                    TablaClasificacionCompleta(standings)
                }
            }*/StandingsWidget(
                leagueId = leagueId,
                season = temporada
            )
            2 -> {
                val equipos = viewModel.equipos.value
                if (equipos.isEmpty()) {
                    Text("No hay equipos disponibles de la temporada $temporadaSeleccionada", modifier = Modifier.padding(16.dp))
                } else {
                    EquiposPorLiga(equipos)
                }

            }
            3 -> {
                val goleadores = viewModel.topScorers.value
                if (goleadores.isEmpty()) {
                    Text("No hay goleadores disponibles de la temporada $temporadaSeleccionada", modifier = Modifier.padding(16.dp))
                } else {
                    TablaGoleadores(goleadores)
                }
            }
            5 -> {
                val info = viewModel.infoLiga.value
                if (info != null) {
                    InfoLigaTab(info)
                } else {
                    Text("Cargando información de la liga...", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }

}

@Composable
fun InfoLigaTab(info: LeagueDetailItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = info.league.logo,
                contentDescription = "Logo Liga",
                modifier = Modifier.size(60.dp)
            )
            Column {
                Text(info.league.name, style = MaterialTheme.typography.titleLarge)
                Text(info.league.type, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = info.country.flag,
                contentDescription = "Bandera país",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(info.country.name, style = MaterialTheme.typography.bodyLarge)
        }

        Divider()

        Text("Temporadas registradas:", fontWeight = FontWeight.Bold)
        LazyColumn {
            items(info.seasons.sortedByDescending { it.year }) { temporada ->
                Text("• ${temporada.year} (${temporada.start} → ${temporada.end})" +
                        if (temporada.current) " 🟢 Actual" else "")
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
fun EquiposPorLiga(equipos: List<com.wikifut.app.model.TeamBasicInfo>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(equipos) { equipo ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = equipo.logo,
                        contentDescription = equipo.name,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = equipo.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}