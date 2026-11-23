package com.gourmet.pizzeriaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingBag
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
import com.gourmet.pizzeriaapp.model.Pizza
import com.gourmet.pizzeriaapp.model.Producto
import com.gourmet.pizzeriaapp.ui.PizzeriaAppState
import com.gourmet.pizzeriaapp.ui.theme.*
import com.gourmet.pizzeriaapp.utilidades.FormatoUtilidades

data class PizzaFigmaItem(
    val id: String,
    val nombre: String,
    val subtitulo: String,
    val badge: String?,
    val badgeColor: Color,
    val badgeBgColor: Color,
    val descripcion: String,
    val precioChica: Double,
    val precioMediana: Double,
    val precioGrande: Double,
    val imageUrl: String,
    val ingredientes: List<String>
)

data class ItemMenuSimple(
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val precio: Double,
    val imageUrl: String
)

@Composable
fun NuevoPedidoFigmaScreen(
    appState: PizzeriaAppState,
    filtroInicial: String? = null,
    onVerCarritoClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf(filtroInicial ?: "Pizzas") }

    val pizzas = listOf(
        PizzaFigmaItem(
            id = "margherita",
            nombre = "Margherita Artesanal",
            subtitulo = "Clásica",
            badge = "Vegetariana",
            badgeColor = FreshGreen,
            badgeBgColor = FreshGreenLight,
            descripcion = "Salsa de tomate San Marzano, mozzarella fior di latte, albahaca fresca y aceite de oliva extra virgen.",
            precioChica = 7990.0,
            precioMediana = 9990.0,
            precioGrande = 12490.0,
            imageUrl = "https://images.unsplash.com/photo-1604382355076-af4b0eb60143?w=800&q=80",
            ingredientes = listOf("Salsa San Marzano", "Mozzarella fior di latte", "Albahaca fresca", "Aceite de oliva")
        ),
        PizzaFigmaItem(
            id = "pepperoni",
            nombre = "Pepperoni Diavola",
            subtitulo = "Especial",
            badge = "Favorita",
            badgeColor = PizzaRedDark,
            badgeBgColor = PizzaRedLight,
            descripcion = "Pepperoni crujiente, mozzarella fundida, chile quebrado y un toque de miel picante de abeja.",
            precioChica = 8490.0,
            precioMediana = 10490.0,
            precioGrande = 12990.0,
            imageUrl = "https://images.unsplash.com/photo-1628840042765-356cda07504e?w=800&q=80",
            ingredientes = listOf("Pepperoni artesanal", "Mozzarella fundida", "Chile quebrado", "Miel de abeja picante")
        ),
        PizzaFigmaItem(
            id = "cuatroquesos",
            nombre = "Cuatro Quesos & Nuez",
            subtitulo = "Bianca",
            badge = "Vegetariana",
            badgeColor = FreshGreen,
            badgeBgColor = FreshGreenLight,
            descripcion = "Mozzarella, gorgonzola dulce, parmesano reggiano, fontina y nuez tostada con pimienta negra.",
            precioChica = 8990.0,
            precioMediana = 10990.0,
            precioGrande = 13490.0,
            imageUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=800&q=80",
            ingredientes = listOf("Mozzarella", "Gorgonzola dulce", "Parmesano reggiano", "Fontina", "Nuez tostada")
        )
    )

    val otrosItems = listOf(
        ItemMenuSimple("Limonada Italiana", "Bebidas", "Limón amarillo mediterráneo con menta fresca.", 2800.0, "https://images.unsplash.com/photo-1551024709-8f23befc6f87?w=500&q=80"),
        ItemMenuSimple("Refresco Coca Cola 600ml", "Bebidas", "Bien fría con hielo.", 1990.0, "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500&q=80"),
        ItemMenuSimple("Cerveza Artesanal", "Bebidas", "Cerveza clara estilo pilsner artesanal.", 3500.0, "https://images.unsplash.com/photo-1608270586620-248524c67de9?w=500&q=80"),
        ItemMenuSimple("Combo Familiar 2x1", "Promos", "2 Pizzas Grandes + Refresco 2L + Ajo Pan.", 18990.0, "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500&q=80"),
        ItemMenuSimple("Martes 2x1 Clásicas", "Promos", "Segunda pizza de igual o menor valor gratis.", 10490.0, "https://images.unsplash.com/photo-1579751626657-72bc17010498?w=500&q=80")
    )

    val pizzasFiltradas = pizzas.filter {
        (categoriaSeleccionada == "Pizzas") &&
        (it.nombre.contains(searchQuery, ignoreCase = true) ||
         it.descripcion.contains(searchQuery, ignoreCase = true) ||
         it.subtitulo.contains(searchQuery, ignoreCase = true))
    }

    val otrosFiltrados = otrosItems.filter {
        (categoriaSeleccionada == it.categoria) &&
        (it.nombre.contains(searchQuery, ignoreCase = true) ||
         it.descripcion.contains(searchQuery, ignoreCase = true))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp)
        ) {
            // BUSCADOR
            item {
                Surface(
                    color = SurfaceCard,
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    "Buscar pizza, ingrediente o bebida...",
                                    fontSize = 13.sp,
                                    color = TextMuted
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        )
                    }
                }
            }

            // CHIPS DE CATEGORÍAS (Pizzas, Bebidas, Promos)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val categorias = listOf("Pizzas", "Bebidas", "Promos")
                    categorias.forEach { cat ->
                        val activa = (categoriaSeleccionada == cat)
                        Surface(
                            color = if (activa) PizzaRed else SurfaceCard,
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .clickable { categoriaSeleccionada = cat }
                        ) {
                            Text(
                                text = cat,
                                color = if (activa) Color.White else TextPrimary,
                                fontWeight = if (activa) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 22.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            // LISTA DE PIZZAS (FIGMA CARDS)
            if (categoriaSeleccionada == "Pizzas") {
                items(pizzasFiltradas, key = { it.id }) { pizza ->
                    PizzaCardFigma(
                        pizza = pizza,
                        onAgregarAlCarrito = { tamanio, precioFinal ->
                            appState.agregarProductoAlCarrito(
                                Pizza("${pizza.nombre} ($tamanio)", precioFinal, pizza.ingredientes),
                                1
                            )
                            appState.mensajeNotificacion = "¡${pizza.nombre} ($tamanio) agregada al carrito!"
                        }
                    )
                }
            } else {
                items(otrosFiltrados, key = { it.nombre }) { itemSimple ->
                    OtroProductoCardFigma(
                        item = itemSimple,
                        onAgregar = {
                            appState.agregarProductoAlCarrito(
                                Producto(itemSimple.nombre, itemSimple.precio, categoria = itemSimple.categoria),
                                1
                            )
                            appState.mensajeNotificacion = "¡${itemSimple.nombre} agregada al carrito!"
                        }
                    )
                }
            }
        }

        // BARRA FLOTANTE DE CARRITO
        val totalProductos = appState.productosEnCarrito.size
        if (totalProductos > 0) {
            val totalMonto = appState.productosEnCarrito.sumOf { it.precio }
            Surface(
                color = CartBarDark,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingBag,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "$totalProductos productos • ${FormatoUtilidades.formatearCLP(totalMonto, incluirMoneda = true)}",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }

                    Button(
                        onClick = onVerCarritoClick,
                        colors = ButtonDefaults.buttonColors(containerColor = PizzaRed),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Ver carrito",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PizzaCardFigma(
    pizza: PizzaFigmaItem,
    onAgregarAlCarrito: (tamanio: String, precio: Double) -> Unit
) {
    var tamanioSeleccionado by remember { mutableStateOf("Mediana") }
    val precioActual = when (tamanioSeleccionado) {
        "Chica" -> pizza.precioChica
        "Grande" -> pizza.precioGrande
        else -> pizza.precioMediana
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Imagen grande con badge en la esquina superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = pizza.imageUrl,
                    contentDescription = pizza.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (pizza.badge != null) {
                    Surface(
                        color = pizza.badgeBgColor,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .padding(14.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = pizza.badge,
                            color = pizza.badgeColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Detalles de la pizza
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pizza.nombre,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = pizza.subtitulo,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PizzaRed
                    )
                }

                Text(
                    text = pizza.descripcion,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )

                // Selectores de tamaño (Chica, Mediana, Grande)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tamanios = listOf("Chica", "Mediana", "Grande")
                    tamanios.forEach { tam ->
                        val seleccionado = (tamanioSeleccionado == tam)
                        Surface(
                            color = if (seleccionado) PizzaRedLight else SurfaceBeigeLight,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { tamanioSeleccionado = tam }
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tam,
                                    fontSize = 12.sp,
                                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
                                    color = if (seleccionado) PizzaRedDark else TextSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Fila de Precio y Botón circular "+"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Precio",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Text(
                            text = FormatoUtilidades.formatearCLP(precioActual, incluirMoneda = true),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(PizzaRed)
                            .clickable { onAgregarAlCarrito(tamanioSeleccionado, precioActual) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OtroProductoCardFigma(
    item: ItemMenuSimple,
    onAgregar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
                Text(
                    text = item.descripcion,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
                Text(
                    text = FormatoUtilidades.formatearCLP(item.precio, incluirMoneda = true),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = PizzaRed
                )
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(PizzaRed)
                    .clickable { onAgregar() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
