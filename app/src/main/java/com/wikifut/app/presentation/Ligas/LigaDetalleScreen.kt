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
import com.wikifut.app.presentation.Ligas.LigaDetalleViewModel

@Composable
fun LigaDetalleScreen(
    leagueId: Int,
    season: Int,
    navController: NavHostController,
    viewModel: LigaDetalleViewModel = hiltViewModel()
) {
    // Estado para el tab seleccionado
    var selectedTab by remember { mutableStateOf(0) }

    // Estado global de tabla
    val tabla = viewModel.tabla.value

    // Llamar solo una vez
    LaunchedEffect(Unit) {
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
                text = "Liga $leagueId", // ❗ Puedes reemplazarlo con un ViewModel más adelante
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = rememberAsyncImagePainter("https://media.api-sports.io/football/leagues/$leagueId.png"),
                contentDescription = "Logo liga",
                modifier = Modifier.size(40.dp)
            )
        }

        // 🔁 Tabs
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Tabla", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Estadísticas", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("Temporada", modifier = Modifier.padding(12.dp))
            }
        }

        // 📋 Contenido según tab
        when (selectedTab) {
            0 -> TablaPosiciones(tabla)
            1 -> Text("Estadísticas (en desarrollo)", modifier = Modifier.padding(16.dp))
            2 -> Text("Temporada actual: $season", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun TablaPosiciones(tabla: List<StandingTeam>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tabla) { team ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${team.rank}. ${team.team.name}")
                Text("${team.points} pts")
            }
        }
    }
}
