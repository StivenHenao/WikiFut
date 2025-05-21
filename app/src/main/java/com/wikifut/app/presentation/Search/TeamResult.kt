package com.wikifut.app.presentation.Search

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wikifut.app.model.Team
import com.wikifut.app.model.Venue


@Composable
fun EquiposResult(viewModel: SearchViewModel, onTeamNavigate: (team: Team, venue: Venue) -> Unit) {
    val resultadoState by viewModel.resultadoEquipos.collectAsState()
    val resultado = resultadoState
    if (resultado == null || resultado.response.isEmpty()) {
        Text(
            text = "No hay resultados",
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn {
            items(resultado.response) { equipo ->
                TeamItem(team = equipo.team, onClick = {onTeamNavigate(equipo.team, equipo.venue)})
            }
        }
    }
}

@Composable
fun TeamItem(team: Team, onClick: () -> Unit) {
    val team_country = team.country ?: ""
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable{onClick()}, // Hacer el Card clickeable
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
                model = team.logo,
                contentDescription = "Logo ${team.name}",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = team.name, style = MaterialTheme.typography.bodyMedium)
                Text(text = team_country, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}