package com.wikifut.app.presentation.Header

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.wikifut.app.R
import com.wikifut.app.model.TipoBusqueda

fun noHacerNada(tipoBusqueda: TipoBusqueda  , string: String) {
}


@Composable
fun Header(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onBuscar: (TipoBusqueda, String) -> Unit,
    actions: @Composable () -> Unit = {}
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Equipos") }
    var modoBusquedaActiva by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val menuOptions = listOf("Equipos", "Ligas", "Partidos", "Jugador")
    val dropdownBackgroundColor = Color(0xFF4A148C)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1F1235))
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.White, shape = CircleShape)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.wikifutlogo),
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp)
            )
        }

        // Buscador + Dropdown
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 3.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { 
                    Text(
                        "🔍 Buscar $selectedOption",
                        style = MaterialTheme.typography.bodyLarge
                    ) 
                },
                modifier = Modifier
                    .weight(1f)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyUp) {
                            focusManager.clearFocus()
                            val tipoBusqueda = when (selectedOption) {
                                "Ligas" -> TipoBusqueda.Ligas
                                "Partidos" -> TipoBusqueda.Partidos
                                "Jugador" -> TipoBusqueda.Jugadores
                                "Equipos" -> TipoBusqueda.Equipos
                                else -> TipoBusqueda.Jugadores
                            }
                            onBuscar(tipoBusqueda, searchQuery)
                            true
                        } else false
                    },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        val tipoBusqueda = when (selectedOption) {
                            "Ligas" -> TipoBusqueda.Ligas
                            "Partidos" -> TipoBusqueda.Partidos
                            "Jugador" -> TipoBusqueda.Jugadores
                            "Equipos" -> TipoBusqueda.Equipos
                            else -> TipoBusqueda.Jugadores
                        }
                        onBuscar(tipoBusqueda, searchQuery)
                    }
                )
            )

            Box(
                modifier = Modifier
                    .background(dropdownBackgroundColor, shape = RoundedCornerShape(6.dp))
            ) {
                IconButton(
                    onClick = { dropdownExpanded = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_drop_down),
                        contentDescription = "Seleccionar tipo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                menuOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        // Iconos de acción
        Row(
            modifier = Modifier.padding(start = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            actions()
        }
    }
}
