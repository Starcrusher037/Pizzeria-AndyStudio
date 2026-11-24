package com.gourmet.pizzeriaapp.ui.screens

/**
 * NOTA: Esta pantalla y sus funciones asociadas de inventario y gestión de repartidores
 * fueron retiradas de la navegación principal del cliente y quedan reservadas para una
 * futura implementación (por ejemplo, panel de administración interno o módulo de cocina).
 */

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gourmet.pizzeriaapp.model.Repartidor
import com.gourmet.pizzeriaapp.ui.PizzeriaAppState

@Composable
fun InventarioScreen(appState: PizzeriaAppState) {
    val stock = appState.stock
    val repartidores = appState.listaRepartidores

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // SECCIÓN INVENTARIO DE INGREDIENTES
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Stock de Inventario",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Insumos para preparación de pizzas",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { appState.reabastecerTodo() },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Reabastecer Todo (+15)", fontSize = 12.sp)
                        }
                    }

                    HorizontalDivider()

                    stock.forEach { (ingrediente, cantidad) ->
                        FilaInventario(
                            ingrediente = ingrediente,
                            cantidad = cantidad,
                            onReabastecer = { appState.reabastecerIngrediente(ingrediente, 10) }
                        )
                    }
                }
            }
        }

        // SECCIÓN REPARTIDORES
        item {
            Text(
                text = "Repartidores y Logística",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(repartidores) { repartidor ->
            RepartidorCard(repartidor = repartidor)
        }
    }
}

@Composable
fun FilaInventario(
    ingrediente: String,
    cantidad: Int,
    onReabastecer: () -> Unit
) {
    val progreso = (cantidad / 20f).coerceIn(0f, 1f)
    val colorBarra = when {
        cantidad <= 2 -> Color(0xFFC62828)
        cantidad <= 5 -> Color(0xFFEF6C00)
        else -> Color(0xFF2E7D32)
    }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ingrediente,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$cantidad unidades",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorBarra
                )
                OutlinedButton(
                    onClick = onReabastecer,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("+10", fontSize = 11.sp)
                }
            }
        }
        LinearProgressIndicator(
            progress = { progreso },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = colorBarra,
            trackColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun RepartidorCard(repartidor: Repartidor) {
    val esCercano = repartidor.distanciaKm <= 2.5

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "${repartidor.idRepartidor}. ${repartidor.nombre}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = "Distancia: ${repartidor.distanciaKm} km ${if (esCercano) "⚡ (Prioridad cercana)" else ""}",
                    fontSize = 12.sp,
                    color = if (esCercano) Color(0xFF1976D2) else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (esCercano) FontWeight.SemiBold else FontWeight.Normal
                )
                Text(
                    text = "Contacto: ${repartidor.telefono} | ${repartidor.email}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                color = if (repartidor.disponible) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (repartidor.disponible) "Disponible" else "En Ruta",
                    color = if (repartidor.disponible) Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
