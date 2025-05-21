package com.wikifut.app.presentation.player.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Contenedor reutilizable para mostrar listas de datos con estilo consistente.
 *
 * @param title Título opcional del contenedor.
 * @param items Lista de datos genéricos a mostrar.
 * @param modifier Modificador para personalizar el layout.
 * @param verticalArrangement Disposición vertical de los elementos (por defecto: Arrangement.Top).
 * @param horizontalAlignment Alineación horizontal (por defecto: Alignment.Start).
 * @param itemSpacing Espacio entre ítems (por defecto: 8.dp).
 * @param content Lambda que define cómo renderizar cada ítem.
 * @param headerContent Contenido opcional arriba de los ítems.
 * @param footerContent Contenido opcional debajo de los ítems.
 */
@Composable
fun <T> DataContainer(
    title: String? = null,
    items: List<T>,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    itemSpacing: Dp = 8.dp,
    content: @Composable (T) -> Unit,
    headerContent: @Composable (() -> Unit)? = null,
    footerContent: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = modifier,
        color = Color(0xFF1E1E1E),  // Color de fondo
        shape = RoundedCornerShape(8.dp),  // Bordes redondeados
        shadowElevation = 4.dp  // Sombra
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            // Título (opcional)
            title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Lista de ítems con espaciado
            Column(
                verticalArrangement = Arrangement.spacedBy(itemSpacing)
            ) {
                items.forEach { item ->
                    content(item)  // Renderiza cada ítem
                }
            }

        }
    }
}