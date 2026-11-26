package com.gourmet.pizzeriaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gourmet.pizzeriaapp.model.Pedido
import com.gourmet.pizzeriaapp.ui.PizzeriaAppState

@Composable
fun ReportePedidosScreen(appState: PizzeriaAppState) {
    var filtroEstado by remember { mutableStateOf("Todos") }

    val pedidos = appState.listaPedidos
    val completados = pedidos.filter { it.estadoPedido == "Listo" || it.estadoPedido == "En camino" }
    val pendientesOCancelados = pedidos.filter { it.estadoPedido == "En preparacion" || it.estadoPedido == "En preparación" || it.estadoPedido == "Cancelado" }
    val recaudacionTotal = completados.sumOf { it.calcularTotal() }

    val pedidosFiltrados = remember(filtroEstado, pedidos.toList()) {
        if (filtroEstado == "Todos") {
            pedidos
        } else {
            pedidos.filter { it.estadoPedido.equals(filtroEstado, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // TARJETA DE REPORTE DIARIO CONSOLIDADO
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Reporte Diario Consolidado",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        EstadisticaItem("Total Registrados", "${pedidos.size}")
                        EstadisticaItem("Completados", "${completados.size}", Color(0xFF2E7D32))
                        EstadisticaItem("Pend/Cancelados", "${pendientesOCancelados.size}", Color(0xFFC62828))
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recaudación Total del Día:",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$${"%.2f".format(recaudacionTotal)}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // CHIPS DE FILTRO POR ESTADO
        item {
            val filtros = listOf("Todos", "En preparacion", "En camino", "Listo", "Cancelado")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filtros) { filtro ->
                    FilterChip(
                        selected = (filtroEstado == filtro),
                        onClick = { filtroEstado = filtro },
                        label = { Text(filtro) }
                    )
                }
            }
        }

        // LISTA DE PEDIDOS
        if (pedidosFiltrados.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay pedidos con el estado '$filtroEstado'",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(pedidosFiltrados, key = { it.idPedido }) { pedido ->
                PedidoCard(
                    pedido = pedido,
                    onCambiarEstado = { nuevoEstado ->
                        appState.cambiarEstadoPedido(pedido, nuevoEstado)
                    },
                    onEntregar = {
                        appState.entregarPedidoConRepartidor(pedido)
                    }
                )
            }
        }
    }
}

@Composable
fun EstadisticaItem(etiqueta: String, valor: String, colorValor: Color = MaterialTheme.colorScheme.onPrimaryContainer) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colorValor)
        Text(text = etiqueta, fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
    }
}

@Composable
fun PedidoCard(
    pedido: Pedido,
    onCambiarEstado: (String) -> Unit,
    onEntregar: () -> Unit
) {
    // Declaraciones de desestructuracion (Paso 4)
    val (clienteExtraido, productosExtraidos, totalCalculado) = pedido
    val (nombreCli, emailCli, telCli) = clienteExtraido

    val colorEstado = when (pedido.estadoPedido.lowercase()) {
        "listo" -> Color(0xFF2E7D32)
        "en camino" -> Color(0xFF1976D2)
        "cancelado" -> Color(0xFFC62828)
        else -> Color(0xFFEF6C00) // En preparacion
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${pedido.idPedido}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Surface(
                    color = colorEstado.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = pedido.estadoPedido,
                        color = colorEstado,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "Cliente: $nombreCli | Tel: ${telCli ?: "Sin tel"} | Email: ${emailCli ?: "Sin correo"}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Dirección: ${clienteExtraido.direccion}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            val resumenProductos = productosExtraidos.groupBy { it.nombre }
                .map { "${it.value.size}x ${it.key}" }
                .joinToString(", ")

            Text(
                text = "Productos (${productosExtraidos.size}): $resumenProductos",
                fontSize = 13.sp
            )

            if (!pedido.notas.isNullOrBlank()) {
                Text(
                    text = "Notas: ${pedido.notas}",
                    fontSize = 12.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fecha: ${pedido.fecha}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Total: $${"%.2f".format(totalCalculado)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Acciones del pedido
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pedido.estadoPedido == "En preparacion" || pedido.estadoPedido == "En preparación") {
                    Button(
                        onClick = onEntregar,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Asignar Repartidor y Entregar", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = { onCambiarEstado("Cancelado") },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Cancelar", fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
                    }
                } else if (pedido.estadoPedido == "En camino") {
                    Button(
                        onClick = { onCambiarEstado("Listo") },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Marcar como Entregado / Listo", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
