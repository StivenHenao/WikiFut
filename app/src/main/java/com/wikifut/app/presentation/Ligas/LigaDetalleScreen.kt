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

    val temporada = viewModel.temporada.value
    val temporadas = viewModel.temporadasDisponibles.value

    var temporadaSeleccionada by remember { mutableStateOf(season) }
    var expanded by remember { mutableStateOf(false) }

    // Llamar solo una vez
    // Llamar al cargar tabla cuando cambia la temporada seleccionada
    LaunchedEffect(temporadaSeleccionada) {
        viewModel.cargarTabla(leagueId, temporadaSeleccionada)
    }
    // Solo una vez
    LaunchedEffect(Unit) {
        viewModel.obtenerTemporadaActual(leagueId)
        viewModel.cargarTabla(leagueId, temporadaSeleccionada)
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

        // 🧭 Tabs: Tabla | Estadísticas | Temporada
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Tabla", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Equipos", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("Temporada actual: $season", modifier = Modifier.padding(12.dp))
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

