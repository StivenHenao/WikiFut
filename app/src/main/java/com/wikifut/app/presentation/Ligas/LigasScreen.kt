package com.wikifut.app.presentation.Ligas

import androidx.compose.foundation.Image
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

@Composable
fun LigasScreen(viewModel: LigasViewModel = hiltViewModel()) {
    val ligas = viewModel.ligas.value

    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(ligas) { liga ->
            LigaItem(liga)
        }
    }
}

@Composable
fun LigaItem(liga: LigaData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
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
                    text = "${liga.country.name} • Temp. ${liga.league.season}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // (Opcional) Bandera del país
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
