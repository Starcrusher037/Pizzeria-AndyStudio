package com.gourmet.pizzeriaapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gourmet.pizzeriaapp.ui.theme.*
/**
 * Barra superior unificada fiel al diseño de Figma:
 * - Logo con icono de pizza y texto "Pizzeria"
 * - Título de la pantalla
 * - Icono de carrito con indicador dinámico de cantidad
 * - Avatar de usuario en círculo rojo
 */
@Composable
fun PizzeriaTopBar(
    titulo: String,
    cantidadCarrito: Int = 0,
    onCarritoClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onNavigateToClientes: () -> Unit = {},
    onNavigateToProductos: () -> Unit = {},
    onNavigateToPedidos: () -> Unit = {}
) {
    var menuDesplegado by remember { mutableStateOf(false) }

    Surface(
        color = CreamBackground,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo + Título
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCard),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalPizza,
                            contentDescription = "Logo",
                            tint = PizzaRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "PIZZERIA",
                            fontSize = 6.sp,
                            fontWeight = FontWeight.Bold,
                            color = PizzaRed
                        )
                    }
                }

                Text(
                    text = titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            // Acciones: Carrito, Perfil y Menú Desplegable
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Carrito con Badge
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable { onCarritoClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingBag,
                        contentDescription = "Carrito",
                        tint = TextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    if (cantidadCarrito > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 2.dp, y = (-2).dp)
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(FreshGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (cantidadCarrito > 9) "9+" else "$cantidadCarrito",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Perfil con círculo rojo oscuro
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PizzaRed)
                        .clickable { onPerfilClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Menú desplegable interactivo con opciones clave e iconos
                Box(contentAlignment = Alignment.Center) {
                    IconButton(
                        onClick = { menuDesplegado = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menú de navegación",
                            tint = TextPrimary
                        )
                    }

                    DropdownMenu(
                        expanded = menuDesplegado,
                        onDismissRequest = { menuDesplegado = false },
                        modifier = Modifier.background(SurfaceCard)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Gestión de Clientes", fontWeight = FontWeight.Medium, color = TextPrimary) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = null,
                                    tint = PizzaRed
                                )
                            },
                            onClick = {
                                menuDesplegado = false
                                onNavigateToClientes()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Catálogo de Productos", fontWeight = FontWeight.Medium, color = TextPrimary) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.LocalPizza,
                                    contentDescription = null,
                                    tint = PizzaRed
                                )
                            },
                            onClick = {
                                menuDesplegado = false
                                onNavigateToProductos()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Gestión de Pedidos", fontWeight = FontWeight.Medium, color = TextPrimary) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                    contentDescription = null,
                                    tint = PizzaRed
                                )
                            },
                            onClick = {
                                menuDesplegado = false
                                onNavigateToPedidos()
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Barra de navegación inferior fiel a Figma:
 * Fondo verde bosque oscuro (#134221), con 5 secciones:
 * Inicio, Nuevo pedido, Pedidos, Promos, Sucursales.
 * Icono y texto activo en verde lima pistache (#C5E86C).
 */
@Composable
fun PizzeriaBottomNavBar(
    pestanaSeleccionada: Int,
    onPestanaSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem(
            titulo = "Inicio",
            iconoSeleccionado = Icons.Default.LocalPizza,
            iconoNoSeleccionado = Icons.Outlined.LocalPizza
        ),
        BottomNavItem(
            titulo = "Nuevo pedido",
            iconoSeleccionado = Icons.Default.AddCircle,
            iconoNoSeleccionado = Icons.Outlined.AddCircleOutline
        ),
        BottomNavItem(
            titulo = "Pedidos",
            iconoSeleccionado = Icons.Default.Inventory2,
            iconoNoSeleccionado = Icons.Outlined.Inventory2
        ),
        BottomNavItem(
            titulo = "Promos",
            iconoSeleccionado = Icons.Default.LocalOffer,
            iconoNoSeleccionado = Icons.Outlined.LocalOffer
        ),
        BottomNavItem(
            titulo = "Sucursales",
            iconoSeleccionado = Icons.Default.LocationOn,
            iconoNoSeleccionado = Icons.Outlined.LocationOn
        )
    )

    Surface(
        color = ForestGreen,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val seleccionado = (pestanaSeleccionada == index)
                val colorIcono = if (seleccionado) LimeActive else NavUnselected
                val colorTexto = if (seleccionado) LimeActiveText else NavUnselected

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onPestanaSelected(index) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (seleccionado) item.iconoSeleccionado else item.iconoNoSeleccionado,
                        contentDescription = item.titulo,
                        tint = colorIcono,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.titulo,
                        fontSize = 10.sp,
                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                        color = colorTexto
                    )
                }
            }
        }
    }
}

private data class BottomNavItem(
    val titulo: String,
    val iconoSeleccionado: ImageVector,
    val iconoNoSeleccionado: ImageVector
)
