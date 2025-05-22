package com.wikifut.app.presentation.Ligas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.wikifut.app.model.LigaData
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.navigation.NavHostController


@Composable
fun LigasScreen(
    viewModel: LigasViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val ligas = viewModel.ligasFiltradas.value
    val query = viewModel.searchQuery.value

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔍 Buscador
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.actualizarBusqueda(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            placeholder = { Text("Buscar por liga o país") },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                }
            }
        )

        // 📋 Lista de Ligas
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(ligas) { liga ->
                LigaItem(liga = liga) {
                    val temporadaActual = liga.seasons.find { it.current }?.year ?: 2024

                    navController.navigate("ligaDetalle/${liga.league.id}/$temporadaActual")
                }
            }
        }
    }
}

@Composable
fun LigaItem(
    liga: LigaData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() }, // 👈 click en toda la tarjeta
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo de la liga
            AsyncImage(
                model = liga.league.logo,
                contentDescription = "Logo ${liga.league.name}",
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Fit
            )

            Column {
                Text(
                    text = liga.league.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${liga.country.name} • Temp. ${liga.seasons.find { it.current }?.year ?: "?"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bandera del país (si existe)
            liga.country.flag?.let { flagUrl ->
                AsyncImage(
                    model = flagUrl,
                    contentDescription = "Bandera ${liga.country.name}",
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}