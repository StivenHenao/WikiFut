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
    // Estado global de tabla
    val tabla = viewModel.tabla.value

    val temporada = viewModel.temporada.value

    // Llamar solo una vez
    LaunchedEffect(Unit) {
        viewModel.obtenerTemporadaActual(leagueId)
        viewModel.cargarTabla(leagueId, season)
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

        // 🧭 Tabs: Tabla | Estadísticas | Temporada
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Tabla", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Estadísticas", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                var expanded by remember { mutableStateOf(false) }
                val temporadas = viewModel.temporadasDisponibles.value

                Box(modifier = Modifier
                    .padding(16.dp)
                    .clickable { expanded = true }
                ) {
                    Text("Temporada: $temporada")
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        temporadas.forEach { year ->
                            DropdownMenuItem(
                                text = { Text("Temporada $year") },
                                onClick = {
                                    expanded = false
                                    viewModel.cargarTabla(leagueId, year)
                                }
                            )
                        }
                    }
                }
            }
        }

        // 📋 Contenido según tab
        when (selectedTab) {
            0 -> StandingsWidget(
                leagueId = leagueId,
                season = temporada
            )
            1 -> Text("Estadísticas (en desarrollo)", modifier = Modifier.padding(16.dp))
            2 -> Text("Temporada actual: $season", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun TablaPosiciones(tabla: List<StandingTeam>) {

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
        // 🧾 Encabezados
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Pos", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
            Text("Equipo", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
            Text("PJ", modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)
            Text("G", modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)
            Text("E", modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)
            Text("P", modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)
            Text("Pts", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold)
        }

        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

        // 📋 Lista de equipos
        LazyColumn {
            items(tabla) { team ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 🥇 Posición
                    Text("${team.rank}", modifier = Modifier.weight(0.5f))

                    // 🛡️ Logo + nombre
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(2f)
                    ) {
                        AsyncImage(
                            model = team.team.logo,
                            contentDescription = team.team.name,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 8.dp)
                        )
                        Text(team.team.name, maxLines = 1)
                    }

                    // 📊 Estadísticas
                    Text("${team.all.played}", modifier = Modifier.weight(0.7f))
                    Text("${team.all.win}", modifier = Modifier.weight(0.7f))
                    Text("${team.all.draw}", modifier = Modifier.weight(0.7f))
                    Text("${team.all.lose}", modifier = Modifier.weight(0.7f))
                    Text("${team.points}", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
