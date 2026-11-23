package com.gourmet.pizzeriaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gourmet.pizzeriaapp.ui.PizzeriaAppState
import com.gourmet.pizzeriaapp.ui.components.CartCheckoutSheet
import com.gourmet.pizzeriaapp.ui.components.PizzeriaBottomNavBar
import com.gourmet.pizzeriaapp.ui.components.PizzeriaTopBar
import com.gourmet.pizzeriaapp.ui.screens.*
import com.gourmet.pizzeriaapp.ui.theme.*
import com.gourmet.pizzeriaapp.uimodule.PizzeriaViewModel
import com.gourmet.pizzeriaapp.utilidades.GestorRegistro
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PizzeriaAppTheme {
                PizzeriaMainScreen()
            }
        }
    }
}

/**
 * Mensaje de bienvenida de la marca.
 */
@Composable
fun Greetings(
    name: String = "",
    modifier: Modifier = Modifier
) {
    Text(
        text = "Bienvenidos a la mejor pizzeria de la ciudad",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f),
        modifier = modifier
    )
}

@Composable
fun Greeting(
    name: String = "Android",
    modifier: Modifier = Modifier
) {
    Greetings(name = name, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingsPreview() {
    PizzeriaAppTheme {
        Greetings()
    }
}

/**
 * Pantalla principal con la arquitectura de diseño de Figma:
 * - 5 secciones orientadas al cliente: Inicio, Nuevo Pedido, Pedidos, Promos, Sucursales.
 * - Se quitan del alcance directo del cliente secciones técnicas internas como Laboratorio.
 * - Carrito flotante y modal checkout unificado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzeriaMainScreen() {
    val appState = remember { PizzeriaAppState() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }
    var categoriaFiltroParaNuevoPedido by remember { mutableStateOf<String?>(null) }
    var mostrarCarritoSheet by remember { mutableStateOf(false) }
    var mostrarPerfilDialog by remember { mutableStateOf(false) }

    // Instancia del ViewModel para sincronización de datos de resumen
    val viewModel: PizzeriaViewModel = viewModel()

    LaunchedEffect(appState.listaPedidos.size) {
        val clientes = GestorRegistro.obtenerClientes()
        val ultimoCliente = clientes.lastOrNull()?.nombre
        viewModel.actualizarResumen(
            totalClientes = clientes.size,
            totalPedidos = appState.listaPedidos.size,
            ultimoCliente = ultimoCliente
        )
    }

    // Notificaciones Snackbar
    LaunchedEffect(appState.mensajeNotificacion) {
        appState.mensajeNotificacion?.let { mensaje ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = mensaje,
                    duration = SnackbarDuration.Short
                )
            }
            appState.mensajeNotificacion = null
        }
    }

    val tituloActual = when (pestanaSeleccionada) {
        0 -> "Inicio"
        1 -> "Nuevo Pedido"
        2 -> "Pedidos"
        3 -> "Promos"
        4 -> "Nuestras Sucursales"
        else -> "Pizzeria Gourmet"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CreamBackground,
        topBar = {
            PizzeriaTopBar(
                titulo = tituloActual,
                cantidadCarrito = appState.productosEnCarrito.size,
                onCarritoClick = { mostrarCarritoSheet = true },
                onPerfilClick = { mostrarPerfilDialog = true }
            )
        },
        bottomBar = {
            PizzeriaBottomNavBar(
                pestanaSeleccionada = pestanaSeleccionada,
                onPestanaSelected = { index ->
                    pestanaSeleccionada = index
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CreamBackground)
        ) {
            when (pestanaSeleccionada) {
                // Pantalla 1 Figma: Inicio
                0 -> InicioScreen(
                    appState = appState,
                    onNavigateToNuevoPedido = { filtro ->
                        categoriaFiltroParaNuevoPedido = filtro
                        pestanaSeleccionada = 1
                    },
                    onNavigateToPromos = {
                        pestanaSeleccionada = 3
                    }
                )
                // Pantalla 2 Figma: Nuevo Pedido
                1 -> NuevoPedidoFigmaScreen(
                    appState = appState,
                    filtroInicial = categoriaFiltroParaNuevoPedido,
                    onVerCarritoClick = { mostrarCarritoSheet = true }
                )
                // Pantalla 3 Figma: Pedidos en curso e históricos
                2 -> PedidosFigmaScreen(
                    appState = appState,
                    onNavigateToNuevoPedido = { pestanaSeleccionada = 1 }
                )
                // Pantalla 4 Figma: Promos que enamoran
                3 -> PromosFigmaScreen(
                    appState = appState,
                    onNavigateToNuevoPedido = { pestanaSeleccionada = 1 }
                )
                // Pantalla 5 Figma: Nuestras Sucursales
                4 -> SucursalesFigmaScreen(
                    appState = appState,
                    onOrdenarAquiClick = { pestanaSeleccionada = 1 }
                )
            }
        }
    }

    // Modal Sheet de Carrito y Checkout
    if (mostrarCarritoSheet) {
        CartCheckoutSheet(
            appState = appState,
            onDismiss = { mostrarCarritoSheet = false },
            onPedidoConfirmado = {
                mostrarCarritoSheet = false
                pestanaSeleccionada = 2 // Redirigir a pestaña Pedidos para ver el estado en vivo
            }
        )
    }

    // Diálogo de Perfil de Cliente
    if (mostrarPerfilDialog) {
        PerfilClienteDialog(
            appState = appState,
            onDismiss = { mostrarPerfilDialog = false }
        )
    }
}

/**
 * Diálogo de información de perfil del cliente.
 */
@Composable
fun PerfilClienteDialog(
    appState: PizzeriaAppState,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PizzaRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text("Mi Perfil Gourmet", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceBeigeLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = if (appState.nombreCliente.isNotBlank()) appState.nombreCliente else "Cliente Gourmet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = if (appState.emailCliente.isNotBlank()) appState.emailCliente else "cliente@pizzeriagourmet.com",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = if (appState.direccionCliente.isNotBlank()) "Dirección: ${appState.direccionCliente}" else "Dirección predeterminada: Providencia, Santiago",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = FreshGreenLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🍕", fontSize = 16.sp)
                        Column {
                            Text("Puntos Pizzalover: 450 pts", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = FreshGreen)
                            Text("¡Tienes 1 pizza mediana gratis por canjear!", fontSize = 11.sp, color = FreshGreen)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PizzaRed),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Aceptar", color = Color.White)
            }
        }
    )
}
