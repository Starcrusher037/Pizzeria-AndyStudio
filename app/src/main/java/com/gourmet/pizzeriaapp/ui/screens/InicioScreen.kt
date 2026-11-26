package com.gourmet.pizzeriaapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gourmet.pizzeriaapp.model.Pizza
import com.gourmet.pizzeriaapp.model.Producto
import com.gourmet.pizzeriaapp.ui.PizzeriaAppState
import com.gourmet.pizzeriaapp.ui.theme.*
import com.gourmet.pizzeriaapp.uimodule.MensajeResumen
import com.gourmet.pizzeriaapp.uimodule.PizzeriaViewModel
import com.gourmet.pizzeriaapp.utilidades.FormatoUtilidades
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun InicioScreen(
    appState: PizzeriaAppState,
    viewModel: PizzeriaViewModel? = null,
    onNavigateToNuevoPedido: (String?) -> Unit,
    onNavigateToPromos: () -> Unit
) {
    val resumenEstado = viewModel?.resumen?.collectAsState()?.value
    var contenidoVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        contenidoVisible = true
    }

    AnimatedVisibility(
        visible = contenidoVisible,
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp)
        ) {
            // HERO BANNER
            item {
                HeroBanner(onPedirAhoraClick = { onNavigateToNuevoPedido(null) })
            }

            // Resumen del sistema con indicador de progreso dinámico
            if (resumenEstado != null) {
                item {
                    AnimatedVisibility(
                        visible = resumenEstado.cargando,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.5.dp,
                                    color = PizzaRed
                                )
                                Column {
                                    Text(
                                        text = resumenEstado.mensajeProgreso.ifBlank { "Cargando datos..." },
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Sincronizando con el módulo de datos",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = !resumenEstado.cargando,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        MensajeResumen(estado = resumenEstado)
                    }
                }
            }

        // EXPLORA EL MENÚ
        item {
            Text(
                text = "Explora el menú",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MenuCategoriaTile(
                    titulo = "Pizzas",
                    colorFondoIcono = Color(0xFFFFEAEA),
                    colorIcono = PizzaRed,
                    icono = Icons.Default.LocalPizza,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToNuevoPedido("Pizzas") }
                )
                MenuCategoriaTile(
                    titulo = "Bebidas",
                    colorFondoIcono = Color(0xFFE3F2FD),
                    colorIcono = Color(0xFF0288D1),
                    icono = Icons.Default.LocalDrink,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToNuevoPedido("Bebidas") }
                )
                MenuCategoriaTile(
                    titulo = "Promos",
                    colorFondoIcono = Color(0xFFE8F5E9),
                    colorIcono = FreshGreen,
                    icono = Icons.Default.LocalOffer,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToPromos
                )
            }
        }

        // LAS MÁS PEDIDAS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Las más pedidas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Ver todo >",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PizzaRed,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onNavigateToNuevoPedido(null) }
                        .padding(4.dp)
                )
            }
        }

        // Items de Las Más Pedidas
        item {
            ItemMasPedida(
                titulo = "Margarita Clásica",
                badge = "Vegetariana",
                colorBadge = FreshGreen,
                descripcion = "Salsa de tomate San Marzano, mozzarella fresca y albahaca.",
                precio = 9990.0,
                imageUrl = "https://images.unsplash.com/photo-1604382355076-af4b0eb60143?w=500&q=80",
                onAgregarClick = {
                    appState.agregarProductoAlCarrito(
                        Pizza("Margarita Clásica", 9990.0, listOf("Salsa San Marzano", "Mozzarella fresca", "Albahaca")),
                        1
                    )
                    appState.mensajeNotificacion = "¡Margarita Clásica agregada al carrito!"
                }
            )
        }

        item {
            ItemMasPedida(
                titulo = "Pepperoni Especial",
                badge = null,
                colorBadge = FreshGreen,
                descripcion = "Doble pepperoni artesanal crujiente, queso mozzarella y orégano.",
                precio = 10490.0,
                imageUrl = "https://images.unsplash.com/photo-1628840042765-356cda07504e?w=500&q=80",
                onAgregarClick = {
                    appState.agregarProductoAlCarrito(
                        Pizza("Pepperoni Especial", 10490.0, listOf("Pepperoni doble", "Mozzarella", "Orégano")),
                        1
                    )
                    appState.mensajeNotificacion = "¡Pepperoni Especial agregada al carrito!"
                }
            )
        }

        item {
            ItemMasPedida(
                titulo = "Cuatro Quesos",
                badge = "Vegetariana",
                colorBadge = FreshGreen,
                descripcion = "Mozzarella, gorgonzola, provolone y parmesano reggiano.",
                precio = 10990.0,
                imageUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&q=80",
                onAgregarClick = {
                    appState.agregarProductoAlCarrito(
                        Pizza("Cuatro Quesos", 10990.0, listOf("Mozzarella", "Gorgonzola", "Provolone", "Parmesano Reggiano")),
                        1
                    )
                    appState.mensajeNotificacion = "¡Cuatro Quesos agregada al carrito!"
                }
            )
        }

        // PROMO DEL DÍA BANNER
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ForestGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        color = Color(0xFFC02626),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Whatshot,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Promo del día",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "2x1 en Pizzas Clásicas",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Solo por hoy, disfruta dos Margarita o Pepperoni al precio de una.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Button(
                        onClick = {
                            appState.agregarProductoAlCarrito(
                                Pizza("2x1 Clásicas Promo", 10490.0, listOf("Margarita", "Pepperoni")),
                                1
                            )
                            appState.mensajeNotificacion = "¡Promo 2x1 aplicada y agregada al carrito!"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PizzaRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Aprovechar promo",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
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
private fun HeroBanner(onPedirAhoraClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1579751626657-72bc17010498?w=800&q=80",
                contentDescription = "Pizza artesanal",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradiente oscuro para contraste de texto
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.35f),
                                Color.Black.copy(alpha = 0.75f)
                            )
                        )
                    )
            )

            // Contenido del Banner
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Badge "Recién salida del horno"
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Recién salida del horno",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "¡Buon appetito! Tu pizza\nartesanal te espera",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 24.sp
                    )

                    Text(
                        text = "Masa madre fermentada 48h y horneada a la leña.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )

                    Button(
                        onClick = onPedirAhoraClick,
                        colors = ButtonDefaults.buttonColors(containerColor = PizzaRed),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Pedir ahora",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuCategoriaTile(
    titulo: String,
    colorFondoIcono: Color,
    colorIcono: Color,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(colorFondoIcono),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = titulo,
                    tint = colorIcono,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titulo,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun ItemMasPedida(
    titulo: String,
    badge: String?,
    colorBadge: Color,
    descripcion: String,
    precio: Double,
    imageUrl: String,
    onAgregarClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Thumbnail
            AsyncImage(
                model = imageUrl,
                contentDescription = titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            // Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = titulo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary,
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (badge != null) {
                        Surface(
                            color = FreshGreenLight,
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Text(
                                text = badge,
                                color = FreshGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = descripcion,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp,
                    maxLines = 2
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = FormatoUtilidades.formatearCLP(precio),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )

                    Button(
                        onClick = onAgregarClick,
                        colors = ButtonDefaults.buttonColors(containerColor = PizzaRed),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "+ Agregar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
