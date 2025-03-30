package com.wikifut.app.presentation.Ligas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Preview(showBackground = true)
@Composable
fun LigasScreen(viewModel: LigasViewModel = hiltViewModel()) {
    val ligas = viewModel.ligas.value

    LazyColumn {
        items(ligas) { liga ->
            Card(modifier = Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = liga.league.logo,
                        contentDescription = liga.league.name,
                        modifier = Modifier.size(50.dp)
                    )
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = liga.league.name, fontWeight = FontWeight.Bold)
                        Text(text = "${liga.country.name} - ${liga.league.season}")
                    }
                }
            }
        }
    }
}
