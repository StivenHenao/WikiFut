package com.wikifut.app.presentation.Liga

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.hilt.navigation.compose.hiltViewModel
import com.wikifut.app.model.StandingTeam
import com.wikifut.app.presentation.Ligas.LigaDetalleViewModel

@Composable
fun LigaDetalleScreen(
    leagueId: Int,
    season: Int,
    viewModel: LigaDetalleViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.cargarTabla(leagueId, season)
    }

    val tabla = viewModel.tabla.value

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Text(
            text = "Tabla de posiciones",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items(tabla) { team ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${team.rank}. ${team.team.name}")
                    Text("${team.points} pts")
                }
            }
        }
    }
}
