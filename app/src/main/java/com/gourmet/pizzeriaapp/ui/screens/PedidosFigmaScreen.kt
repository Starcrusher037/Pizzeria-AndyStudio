package com.gourmet.pizzeriaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gourmet.pizzeriaapp.model.Pedido
import com.gourmet.pizzeriaapp.model.Pizza
import com.gourmet.pizzeriaapp.ui.PizzeriaAppState
import com.gourmet.pizzeriaapp.ui.theme.*
import com.gourmet.pizzeriaapp.utilidades.FormatoUtilidades

data class PedidoHistoricoFigma(
    val id: String,
    val fecha: String,
    val titulo: String,
    val total: Double,
    val estado: String,
    val badgeTexto: String,
    val esBadgeVerde: Boolean,
    val imageUrl: String
)

@Composable
fun PedidosFigmaScreen(
    appState: PizzeriaAppState,
    onNavigateToNuevoPedido: () -> Unit
) {
    val pedidoActivo = appState.listaPedidos.firstOrNull()

    val pedidosHistoricos = listOf(
        PedidoHistoricoFigma(
            id = "#4821",
            fecha = "14 oct",
            titulo = "1 Pizza Margarita + 1 Tiramisú",
            total = 13990.0,
            estado = "Entregado",
            badgeTexto = "✓ Entregado a tiempo",
            esBadgeVerde = true,
            imageUrl = "https://images.unsplash.com/photo-1604382355076-af4b0eb60143?w=500&q=80"
        ),
        PedidoHistoricoFigma(
            id = "#4756",
            fecha = "02 oct",
            titulo = "2 Pizzas Cuatro Quesos + 1 Refresco",
            total = 24990.0,
            estado = "Entregado",
            badgeTexto = "★ Calificado 5.0",
            esBadgeVerde = false,
            imageUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&q=80"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
    ) {
        // ENCABEZADO "Pedido en curso" + BADGE "En vivo"
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido en curso",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Surface(
                    color = FreshGreenLight,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(FreshGreen)
                        )
                        Text(
                            text = "En vivo",
                            color = FreshGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // TARJETA DE PEDIDO EN CURSO
        item {
            if (pedidoActivo != null) {
                val pasoActual = when (pedidoActivo.estadoPedido) {
                    "En preparacion" -> 0
                    "En el horno" -> 1
                    "En camino" -> 2
                    "Listo", "Entregado" -> 3
                    else -> 1
                }
                val estadoTitulo = when (pedidoActivo.estadoPedido) {
                    "En preparacion" -> "Recibido"
                    "En el horno" -> "En el horno"
                    "En camino" -> "En camino"
                    "Listo", "Entregado" -> "Entregado"
                    else -> pedidoActivo.estadoPedido
                }
                val estadoSubtitulo = when (pedidoActivo.estadoPedido) {
                    "En preparacion" -> "Tu pedido fue recibido y está en cola de cocina"
                    "En el horno" -> "Tu masa artesanal se está horneando"
                    "En camino" -> "El repartidor va en camino a tu dirección"
                    "Listo", "Entregado" -> "Tu pedido ha sido completado y entregado"
                    else -> "Procesando pedido"
                }
                val tiempoEstimado = when (pedidoActivo.estadoPedido) {
                    "En preparacion", "En el horno" -> "20–25 min"
                    "En camino" -> "10–15 min"
                    "Listo", "Entregado" -> "Completado"
                    else -> "20 min"
                }
                val resumenProductos = pedidoActivo.productos
                    .groupBy { it.nombre }
                    .entries
                    .joinToString(" + ") { "${it.value.size}x ${it.key}" }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Estado actual y tiempo estimado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(PizzaRedLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Whatshot,
                                        contentDescription = "Estado",
                                        tint = PizzaRed,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1f, fill = false)
                                ) {
                                    Text(
                                        text = estadoTitulo,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = estadoSubtitulo,
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "Tiempo estimado",
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                                Text(
                                    text = tiempoEstimado,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PizzaRed,
                                    maxLines = 1
                                )
                            }
                        }

                        // Barra de progreso y pasos
                        ProgressTimeline(pasoActual = pasoActual)

                        // Tarjeta interna con resumen de productos
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = SurfaceBeigeLight,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                AsyncImage(
                                    model = "https://images.unsplash.com/photo-1628840042765-356cda07504e?w=500&q=80",
                                    contentDescription = "Pedido",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = resumenProductos.ifBlank { "Productos del pedido" },
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        maxLines = 2,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Entrega: ${pedidoActivo.cliente.direccion}",
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        lineHeight = 14.sp,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Total: ${FormatoUtilidades.formatearCLP(pedidoActivo.calcularTotal(), incluirMoneda = true)}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        // Sucursal y botón Actualizar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = FreshGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Pedido #${pedidoActivo.idPedido} · Sucursal Providencia",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }

                            Button(
                                onClick = {
                                    val siguienteEstado = when (pedidoActivo.estadoPedido) {
                                        "En preparacion" -> "En el horno"
                                        "En el horno" -> "En camino"
                                        "En camino" -> "Listo"
                                        else -> "En preparacion"
                                    }
                                    appState.cambiarEstadoPedido(pedidoActivo, siguienteEstado)
                                    appState.mensajeNotificacion = "Estado de pedido #${pedidoActivo.idPedido}: $siguienteEstado"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SurfaceBeige),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Actualizar",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "No tienes pedidos en curso",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Cuando realices un pedido podrás seguir su preparación y entrega en tiempo real.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = onNavigateToNuevoPedido,
                            colors = ButtonDefaults.buttonColors(containerColor = PizzaRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Hacer un pedido", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // SECCIÓN "Pedidos anteriores"
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedidos anteriores",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Últimos 30 días",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // Histórico de pedidos Figma
        items(pedidosHistoricos, key = { it.id }) { historico ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = historico.imageUrl,
                            contentDescription = historico.titulo,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(68.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "${historico.fecha} · Pedido ${historico.id}",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                            Text(
                                text = historico.titulo,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${FormatoUtilidades.formatearCLP(historico.total)} · ${historico.estado}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Badge estado/calificación
                        Surface(
                            color = if (historico.esBadgeVerde) FreshGreenLight else Color(0xFFFFF3E0),
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Text(
                                text = historico.badgeTexto,
                                color = if (historico.esBadgeVerde) FreshGreen else Color(0xFFE65100),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        // Botón Volver a pedir
                        Button(
                            onClick = {
                                appState.agregarProductoAlCarrito(
                                    Pizza(historico.titulo, historico.total, listOf("Masa", "Queso")),
                                    1
                                )
                                appState.mensajeNotificacion = "¡Items del pedido ${historico.id} agregados al carrito!"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PizzaRed),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Volver a pedir",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Otros pedidos registrados previamente:
        val otrosPedidos = appState.listaPedidos.drop(1)
        if (otrosPedidos.isNotEmpty()) {
            item {
                Text(
                    text = "Tus otros pedidos registrados",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(otrosPedidos, key = { it.idPedido }) { pedido ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Pedido #${pedido.idPedido} · ${pedido.estadoPedido}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "${pedido.productos.size} productos · Total: ${FormatoUtilidades.formatearCLP(pedido.calcularTotal())}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                        Surface(
                            color = if (pedido.estadoPedido == "Listo" || pedido.estadoPedido == "Entregado") FreshGreenLight else PizzaRedLight,
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Text(
                                text = pedido.estadoPedido,
                                color = if (pedido.estadoPedido == "Listo" || pedido.estadoPedido == "Entregado") FreshGreen else PizzaRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressTimeline(pasoActual: Int) {
    val pasos = listOf("Recibido", "En el horno", "En camino", "Entregado")

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        // Barra segmentada
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            pasos.forEachIndexed { index, _ ->
                val colorSegmento = when {
                    index < pasoActual -> FreshGreen
                    index == pasoActual -> PizzaRed
                    else -> Color(0xFFEADBCE)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(3.dp))
                        .background(colorSegmento)
                )
            }
        }

        // Nombres de los pasos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            pasos.forEachIndexed { index, nombre ->
                val colorTexto = when {
                    index < pasoActual -> FreshGreen
                    index == pasoActual -> PizzaRed
                    else -> TextMuted
                }
                Text(
                    text = nombre,
                    fontSize = 10.sp,
                    fontWeight = if (index <= pasoActual) FontWeight.Bold else FontWeight.Normal,
                    color = colorTexto
                )
            }
        }
    }
}
