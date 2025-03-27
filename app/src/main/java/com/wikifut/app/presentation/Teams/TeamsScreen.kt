package com.wikifut.app.presentation.Teams

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.wikifut.app.R
import com.wikifut.app.model.Team

@Composable
fun TeamsScreen(team: Team, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1D0B27))
    ) {
        // Sección con imagen de fondo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Altura del fondo
        ) {
            // Imagen de fondo
            Image(
                painter = painterResource(id = R.drawable.bg_team_header), // Asegúrate de tener esta imagen
                contentDescription = "Fondo del equipo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Contenido sobre la imagen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Barra superior con botón de atrás y acciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_24),
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }

                    Row {
                        IconButton(onClick = { /* Acción de favorito */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = "Favorito",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { /* Acción de notificaciones */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_notifications_24),
                                contentDescription = "Notificaciones",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Logo y nombre del equipo centrado
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(team.logo),
                        contentDescription = "Logo de ${team.name}",
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.White, shape = CircleShape)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = team.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Espacio vacío para información futura
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Aquí irá la información sobre jugadores, ligas, estadísticas, etc.",
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}
