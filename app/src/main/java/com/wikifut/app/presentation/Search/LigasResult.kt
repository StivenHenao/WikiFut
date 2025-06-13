package com.wikifut.app.presentation.Search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.wikifut.app.model.League
import com.wikifut.app.model.LigaResponse
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.wikifut.app.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape

private val GrisMorado = Color(0xFF3A2C4A)

@Composable
fun LigasResult(viewModel: SearchViewModel, onLigasNavigate: (leagueId: League, season: Int) -> Unit) {
    val resultadoState by viewModel.resultadoLigas.collectAsState()
    val resultado = resultadoState

    if (resultado == null || resultado.response.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "No hay ligas disponibles en este momento",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp)
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.wikifutfondo1),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(resultado.response) { ligaResponse ->
                    val ligaOriginal = ligaResponse.league
                    val ligaConDatosCompletos = ligaOriginal.copy(
                        country = ligaResponse.country.name,
                        flag = ligaResponse.country.flag
                    )
                    LeagueItem(
                        league = ligaConDatosCompletos,
                        onClick = { onLigasNavigate(ligaConDatosCompletos, 2025) }
                    )
                }
            }
        }
    }
}

@Composable
fun LeagueItem(league: League, onClick: () -> Unit) {
    val name = league.name
    val logo = league.logo
    val country = league.country

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MoradoOscuro),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable{onClick()},
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = logo,
                    contentDescription = "Logo $name",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = name, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                Text(text = country, style = MaterialTheme.typography.bodySmall, color = Color.White)
            }
        }
    }
}
