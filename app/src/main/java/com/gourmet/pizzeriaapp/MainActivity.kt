package com.gourmet.pizzeriaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gourmet.pizzeriaapp.ui.PizzeriaAppState
import com.gourmet.pizzeriaapp.ui.screens.InventarioScreen
import com.gourmet.pizzeriaapp.ui.screens.LaboratorioScreen
import com.gourmet.pizzeriaapp.ui.screens.NuevoPedidoScreen
import com.gourmet.pizzeriaapp.ui.screens.ReportePedidosScreen
import com.gourmet.pizzeriaapp.ui.theme.PizzeriaAppTheme
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
 * Al ejecutar muestra el mensaje "Bienvenidos a la mejor pizzería de la ciudad".
 */
@Composable
fun Greetings(
    name: String = "",
    modifier: Modifier = Modifier
) {
    Text(
        text = "Bienvenidos a la mejor pizzería de la ciudad",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f),
        modifier = modifier
    )
}

/**
 * Sobrecarga o compatibilidad con la plantilla estándar Greeting(name).
 */
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzeriaMainScreen() {
    val appState = remember { PizzeriaAppState() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

    // Mostrar notificaciones emergentes de PizzeriaAppState
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Pizzería Gourmet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Greetings(
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                NavigationBarItem(
                    selected = (pestanaSeleccionada == 0),
                    onClick = { pestanaSeleccionada = 0 },
                    icon = { Text(" ", fontSize = 20.sp) },
                    label = { Text("Nuevo Pedido", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = (pestanaSeleccionada == 1),
                    onClick = { pestanaSeleccionada = 1 },
                    icon = { Text(" ", fontSize = 20.sp) },
                    label = { Text("Pedidos (${appState.listaPedidos.size})", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = (pestanaSeleccionada == 2),
                    onClick = { pestanaSeleccionada = 2 },
                    icon = { Text(" ", fontSize = 20.sp) },
                    label = { Text("Inventario", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = (pestanaSeleccionada == 3),
                    onClick = { pestanaSeleccionada = 3 },
                    icon = { Text(" ", fontSize = 20.sp) },
                    label = { Text("Laboratorio", fontSize = 11.sp) }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (pestanaSeleccionada) {
                0 -> NuevoPedidoScreen(appState = appState)
                1 -> ReportePedidosScreen(appState = appState)
                2 -> InventarioScreen(appState = appState)
                3 -> LaboratorioScreen()
            }
        }
    }
}
