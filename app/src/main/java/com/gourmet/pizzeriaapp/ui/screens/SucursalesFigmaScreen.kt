package com.gourmet.pizzeriaapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gourmet.pizzeriaapp.ui.PizzeriaAppState
import com.gourmet.pizzeriaapp.ui.theme.*

data class SucursalFigmaItem(
    val id: String,
    val nombre: String,
    val distancia: String,
    val direccion: String,
    val horario: String,
    val abierta: Boolean,
    val imageUrl: String
)

@Composable
fun SucursalesFigmaScreen(
    appState: PizzeriaAppState,
    onOrdenarAquiClick: () -> Unit
) {
    var querySucursal by remember { mutableStateOf("") }

    val sucursales = listOf(
        SucursalFigmaItem(
            id = "providencia",
            nombre = "Sucursal Providencia",
            distancia = "1.2 km",
            direccion = "Av. Providencia 1234, Providencia, Santiago",
            horario = "12:00 – 23:00 hrs",
            abierta = true,
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=500&q=80"
        ),
        SucursalFigmaItem(
            id = "lascondes",
            nombre = "Sucursal Las Condes",
            distancia = "2.4 km",
            direccion = "Av. Apoquindo 4500, Las Condes, Santiago",
            horario = "13:00 – 23:30 hrs",
            abierta = true,
            imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=500&q=80"
        ),
        SucursalFigmaItem(
            id = "centro",
            nombre = "Sucursal Santiago Centro",
            distancia = "3.8 km",
            direccion = "Paseo Ahumada 250, Santiago Centro",
            horario = "11:30 – 22:00 hrs",
            abierta = true,
            imageUrl = "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=500&q=80"
        )
    )

    val filtradas = sucursales.filter {
        it.nombre.contains(querySucursal, ignoreCase = true) ||
        it.direccion.contains(querySucursal, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
    ) {
        // MAPA ESTILIZADO DE SUCURSALES (FIGMA)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Fondo estilizado de mapa con avenidas y parques
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRect(color = Color(0xFFE8ECEF))
                        // Áreas verdes / parques
                        drawCircle(color = Color(0xFFD2E8D5), center = Offset(size.width * 0.2f, size.height * 0.4f), radius = 60f)
                        drawCircle(color = Color(0xFFD2E8D5), center = Offset(size.width * 0.75f, size.height * 0.7f), radius = 80f)

                        // Calles principales
                        val streetColor = Color(0xFFFFFFFF)
                        drawLine(streetColor, start = Offset(0f, size.height * 0.35f), end = Offset(size.width, size.height * 0.45f), strokeWidth = 14f)
                        drawLine(streetColor, start = Offset(0f, size.height * 0.7f), end = Offset(size.width, size.height * 0.6f), strokeWidth = 12f)
                        drawLine(streetColor, start = Offset(size.width * 0.3f, 0f), end = Offset(size.width * 0.25f, size.height), strokeWidth = 12f)
                        drawLine(streetColor, start = Offset(size.width * 0.65f, 0f), end = Offset(size.width * 0.6f, size.height), strokeWidth = 14f)
                        drawLine(streetColor, start = Offset(0f, 0f), end = Offset(size.width, size.height), strokeWidth = 8f)
                    }

                    // Pines de sucursales en el mapa
                    // Pin 1: Centro (arriba a la izquierda)
                    PinMarcadorMapa(
                        nombre = "Santiago Centro",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = 40.dp, y = 20.dp)
                    )

                    // Pin 2: Providencia (centro)
                    PinMarcadorMapa(
                        nombre = "Providencia",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = 10.dp, y = (-5).dp)
                    )

                    // Pin 3: Las Condes (abajo a la derecha)
                    PinMarcadorMapa(
                        nombre = "Las Condes",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-50).dp, y = (-48).dp)
                    )

                    // Pill flotante "🟢 3 hornos encendidos cerca de ti"
                    Surface(
                        color = Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(50.dp),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(FreshGreen)
                            )
                            Text(
                                text = "3 hornos encendidos cerca de ti",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // BUSCADOR POR COLONIA O CÓDIGO POSTAL
        item {
            TextField(
                value = querySucursal,
                onValueChange = { querySucursal = it },
                placeholder = {
                    Text(
                        "Buscar por colonia o código postal...",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceCard,
                    unfocusedContainerColor = SurfaceCard,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLeadingIconColor = TextSecondary,
                    unfocusedLeadingIconColor = TextSecondary
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // TÍTULO "Nuestras sucursales" + "3 disponibles"
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nuestras sucursales",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${filtradas.size} disponibles",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // TARJETAS DE CADA SUCURSAL
        items(filtradas, key = { it.id }) { sucursal ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        AsyncImage(
                            model = sucursal.imageUrl,
                            contentDescription = sucursal.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = sucursal.nombre,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                                Surface(
                                    color = SurfaceBeigeLight,
                                    shape = RoundedCornerShape(50.dp),
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    Text(
                                        text = sucursal.distancia,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextSecondary,
                                        maxLines = 1,
                                        softWrap = false,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = sucursal.direccion,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // Estado Abierto y Horario
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = FreshGreenLight,
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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
                                    text = "Abierto ahora",
                                    color = FreshGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = sucursal.horario,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // Botones de acción ("Cómo llegar" y "Ordenar aquí")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                appState.mensajeNotificacion = "Abriendo indicaciones GPS para ${sucursal.nombre}..."
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceBeige),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Directions,
                                    contentDescription = null,
                                    tint = TextPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Cómo llegar",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Button(
                            onClick = {
                                appState.mensajeNotificacion = "Sucursal seleccionada: ${sucursal.nombre}"
                                onOrdenarAquiClick()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PizzaRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Ordenar aquí",
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
    }
}

@Composable
private fun PinMarcadorMapa(
    nombre: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(PizzaRed),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = nombre,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(6.dp),
            shadowElevation = 2.dp,
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Text(
                text = nombre,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}
