package com.wikifut.app.presentation.Search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wikifut.app.model.Player

@Composable
fun PlayerResult(viewModel: SearchViewModel) {
    val resultadoState by viewModel.resultadoJugadores.collectAsState()
    val resultado = resultadoState
    if (resultado == null || resultado.response.isEmpty()) {
        Text(
            text = "No hay resultados de jugadores",
            color = Color.White
        )
    } else {
        LazyColumn {
            items(resultado.response) { jugadorResponse ->
                PlayerItem(player = jugadorResponse.player)
            }
        }
    }
}


@Composable
fun PlayerItem(player: Player) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MoradoClaro),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = player.photo,
                contentDescription = "Foto de ${player.name}",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${player.age} años - ${player.nationality}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Posición: ${player.position}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
