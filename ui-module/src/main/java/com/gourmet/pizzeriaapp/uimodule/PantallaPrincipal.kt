package com.gourmet.pizzeriaapp.uimodule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla inicial que muestra el resumen dinamico del sistema.
 *
 * Presenta:
 * - Numero total de clientes registrados.
 * - Numero total de pedidos realizados.
 * - Nombre del ultimo cliente registrado.
 *
 * Los datos se actualizan automaticamente cada vez que el estado del ViewModel cambia,
 * gracias a la integracion de StateFlow con collectAsState() de Jetpack Compose.
 *
 * @param viewModel ViewModel que provee el estado del resumen.
 */
@Composable
fun PantallaPrincipal(viewModel: PizzeriaViewModel) {
    val estado by viewModel.resumen.collectAsState()
    PantallaPrincipalContenido(estado = estado)
}

/**
 * Composable sin estado (stateless) que renderiza el contenido del resumen.
 * Se separa del ViewModel para facilitar la previsualizacion y las pruebas unitarias.
 *
 * @param estado Datos del resumen a mostrar en pantalla.
 */
@Composable
fun PantallaPrincipalContenido(estado: ResumenEstado) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (estado.cargando) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Encabezado de la seccion
                Text(
                    text = "Resumen del Sistema",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Datos actualizados en tiempo real segun la actividad registrada.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tarjetas de estadisticas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TarjetaEstadistica(
                        etiqueta = "Clientes registrados",
                        valor = estado.totalClientes.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    TarjetaEstadistica(
                        etiqueta = "Pedidos realizados",
                        valor = estado.totalPedidos.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tarjeta del ultimo cliente
                TarjetaUltimoCliente(nombreCliente = estado.ultimoCliente)

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mensaje de resumen completo
                MensajeResumen(estado = estado)
            }
        }
    }
}

/**
 * Tarjeta de estadistica numerica con etiqueta descriptiva.
 *
 * @param etiqueta Texto descriptivo del dato mostrado.
 * @param valor    Valor numerico a destacar.
 * @param modifier Modificador de Compose para control de layout externo.
 */
@Composable
private fun TarjetaEstadistica(
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = valor,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = etiqueta,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Tarjeta que muestra el nombre del ultimo cliente registrado en el sistema.
 *
 * @param nombreCliente Nombre del ultimo cliente, o null si no hay registros.
 */
@Composable
private fun TarjetaUltimoCliente(nombreCliente: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (nombreCliente != null) nombreCliente.first().uppercase() else "-",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Ultimo cliente registrado",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = nombreCliente ?: "Sin registros aun",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/**
 * Composable que genera y muestra el mensaje de resumen del sistema.
 *
 * El mensaje incluye:
 * - El numero total de clientes registrados.
 * - El numero total de pedidos realizados.
 * - El nombre del ultimo cliente registrado.
 *
 * Se actualiza dinamicamente cada vez que el estado del ViewModel cambia.
 *
 * @param estado Estado actual del resumen.
 */
@Composable
fun MensajeResumen(estado: ResumenEstado) {
    val textoUltimoCliente = estado.ultimoCliente ?: "sin clientes registrados"
    val mensajeResumen = buildString {
        append("Sistema de Pizzeria Gourmet: ")
        append("hay ${estado.totalClientes} ")
        append(if (estado.totalClientes == 1) "cliente registrado" else "clientes registrados")
        append(" y ${estado.totalPedidos} ")
        append(if (estado.totalPedidos == 1) "pedido realizado" else "pedidos realizados")
        append(". Ultimo cliente: $textoUltimoCliente.")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Mensaje de resumen",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = mensajeResumen,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PantallaPrincipalPreview() {
    PantallaPrincipalContenido(
        estado = ResumenEstado(
            totalClientes = 3,
            totalPedidos = 7,
            ultimoCliente = "Martin Silva"
        )
    )
}
