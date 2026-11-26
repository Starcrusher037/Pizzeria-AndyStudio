package com.gourmet.pizzeriaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gourmet.pizzeriaapp.model.Producto
import com.gourmet.pizzeriaapp.service.CatalogoProductos
import com.gourmet.pizzeriaapp.ui.PizzeriaAppState
import com.gourmet.pizzeriaapp.util.ValidadorEntrada
import com.gourmet.pizzeriaapp.utilidades.FormatoUtilidades
import com.gourmet.pizzeriaapp.utilidades.PromocionFechaHelper
import com.gourmet.pizzeriaapp.utilidades.Promocionable
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoPedidoScreen(appState: PizzeriaAppState) {
    var mostrarDialogoCombinar by remember { mutableStateOf(false) }
    val pedidoPreview = appState.obtenerPedidoPreview()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Banner Periodo Promocional Activo (Paso 1: Ranges)
        item {
            val hoy = LocalDate.now()
            val enPromo = PromocionFechaHelper.estaEnPeriodoPromocion(hoy)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (enPromo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Periodo Promocional Vigente (Ranges)",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Rango activo: ${PromocionFechaHelper.PERIODO_PROMOCIONAL.start} al ${PromocionFechaHelper.PERIODO_PROMOCIONAL.endInclusive}",
                        fontSize = 12.sp
                    )
                    Text(
                        text = if (enPromo) "¡Descuento de 10% adicional por fecha activo!" else "ℹHoy no aplica descuento por rango de fechas.",
                        fontWeight = FontWeight.Medium,
                        color = if (enPromo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // SECCIÓN 1: Datos del Cliente (Paso 1 y 4)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "1. Datos del Cliente",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = appState.nombreCliente,
                        onValueChange = { appState.nombreCliente = it },
                        label = { Text("Nombre del Cliente *") },
                        placeholder = { Text("Ej: Valentina Carrasco") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = appState.telefonoCliente,
                            onValueChange = { appState.telefonoCliente = it },
                            label = { Text("Teléfono (Opcional)") },
                            placeholder = { Text("Ej: 98765432") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                appState.telefonoCliente = FormatoUtilidades.formatearTelefono(appState.telefonoCliente)
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Text("Formatear", fontSize = 12.sp)
                        }
                    }

                    OutlinedTextField(
                        value = appState.emailCliente,
                        onValueChange = { appState.emailCliente = it },
                        label = { Text("Correo Electrónico (Opcional)") },
                        placeholder = { Text("nombre@dominio.com") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = appState.direccionCliente,
                        onValueChange = { appState.direccionCliente = it },
                        label = { Text("Dirección de Entrega *") },
                        placeholder = { Text("Ej: Av. Las Heras 1234") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }

        // SECCIÓN 2: Catálogo de Productos Disponibles
        item {
            Text(
                text = "2. Catálogo de Productos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(CatalogoProductos.catalogo) { producto ->
            ProductoCard(producto = producto, onAgregar = { cantidad ->
                appState.agregarProductoAlCarrito(producto, cantidad)
            })
        }

        // SECCIÓN 3: Resumen del Pedido y Carrito
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "3. Ítems del Pedido (${appState.productosEnCarrito.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (appState.productosEnCarrito.isNotEmpty()) {
                            TextButton(onClick = { appState.limpiarCarrito() }) {
                                Text("Vaciar", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }

                    if (appState.productosEnCarrito.isEmpty()) {
                        Text(
                            text = "No has agregado productos al pedido aún.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        val agrupados = appState.productosEnCarrito.groupBy { it.nombre }
                        for ((nombre, items) in agrupados) {
                            val primerProd = items.first()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${items.size}x $nombre",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "$${"%.2f".format(primerProd.precio * items.size)} ($${"%.2f".format(primerProd.precio)} c/u)",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { appState.removerProductoDelCarrito(primerProd) }) {
                                    Text("➖", fontSize = 16.sp)
                                }
                            }
                            HorizontalDivider()
                        }

                        OutlinedTextField(
                            value = appState.notasPedido,
                            onValueChange = { appState.notasPedido = it },
                            label = { Text("Notas especiales (Opcional)") },
                            placeholder = { Text("Ej: Sin cebolla, tocar timbre 2B") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Botón para Combinar Pedido usando operador '+' (Paso 3)
                        OutlinedButton(
                            onClick = { mostrarDialogoCombinar = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("➕ Combinar con Pedido Adicional (Operador '+')")
                        }
                    }
                }
            }
        }

        // SECCIÓN 4: Desglose de Descuentos y Total Final (Paso 4: Desestructuración)
        if (pedidoPreview != null) {
            item {
                val (clienteExtraido, productosExtraidos, totalCalculado) = pedidoPreview
                val (nombreCli, emailCli, telCli) = clienteExtraido

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Desglose de Totales y Descuentos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Cliente (Desestructurado): $nombreCli | ${telCli ?: "Sin tel"} | ${emailCli ?: "Sin email"}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalDivider()

                        FilaTotal("Subtotal Bruto", "$${"%.2f".format(pedidoPreview.calcularSubtotal())}")

                        val descVol = pedidoPreview.calcularDescuentoVolumen()
                        if (descVol > 0.0) {
                            FilaTotal("Descuento Volumen (>3 productos, 10%)", "-$${"%.2f".format(descVol)}", Color(0xFF2E7D32))
                        }

                        val descFecha = pedidoPreview.calcularDescuentoPorFecha()
                        if (descFecha > 0.0) {
                            FilaTotal("Descuento Periodo Promo (10%)", "-$${"%.2f".format(descFecha)}", Color(0xFF2E7D32))
                        }

                        val descAnotacion = pedidoPreview.calcularDescuentoPorAnotacion()
                        if (descAnotacion > 0.0) {
                            FilaTotal("Descuento @Promocionable (Reflection)", "-$${"%.2f".format(descAnotacion)}", Color(0xFF2E7D32))
                        }

                        FilaTotal("Descuento Total Acumulado", "-$${"%.2f".format(pedidoPreview.calcularDescuento())}", Color(0xFF2E7D32))
                        FilaTotal("IVA incluido (19%)", "$${"%.2f".format(pedidoPreview.calcularImpuestos())}")
                        HorizontalDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TOTAL FINAL (Desestructurado):",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "$${"%.2f".format(totalCalculado)}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { appState.confirmarPedido() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Confirmar y Registrar Pedido", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Diálogo para Combinar con Pedido Adicional utilizando sobrecarga del operador '+'
    if (mostrarDialogoCombinar) {
        DialogoCombinarPedido(
            onDismiss = { mostrarDialogoCombinar = false },
            onConfirmar = { productosExtra, notasExtra ->
                appState.combinarPedidoConAdicion(productosExtra, notasExtra)
                mostrarDialogoCombinar = false
            }
        )
    }

    // Diálogo de error por stock insuficiente
    if (appState.errorStockFaltante != null) {
        AlertDialog(
            onDismissRequest = { appState.errorStockFaltante = null },
            title = { Text("Stock Insuficiente") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("No hay suficientes insumos en inventario para procesar el pedido:")
                    appState.errorStockFaltante?.forEach {
                        Text("• $it", color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Puedes presionar 'Reabastecer Todo' para recargar los ingredientes de la simulación.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    appState.reabastecerTodo()
                }) {
                    Text("Reabastecer Todo (+15)")
                }
            },
            dismissButton = {
                TextButton(onClick = { appState.errorStockFaltante = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    onAgregar: (Int) -> Unit
) {
    var cantidad by remember { mutableIntStateOf(1) }
    val esPromo = producto::class.java.isAnnotationPresent(Promocionable::class.java)
    val anotacion = if (esPromo) producto::class.java.getAnnotation(Promocionable::class.java) else null

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Categoría: ${producto.categoria} | $${"%.2f".format(producto.precio)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (anotacion != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "PROMO -${(anotacion.descuentoAdicional * 100).toInt()}%",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (producto.ingredientes.isNotEmpty()) {
                Text(
                    text = "Ingredientes: ${producto.ingredientes.joinToString(", ")}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Selector de cantidad en rango 1..100 (Paso 1)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { if (cantidad > 1) cantidad-- },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("−", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "$cantidad",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                    IconButton(
                        onClick = { if (cantidad < 100) cantidad++ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = {
                        onAgregar(cantidad)
                        cantidad = 1
                    },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text("Agregar")
                }
            }
        }
    }
}

@Composable
fun FilaTotal(etiqueta: String, valor: String, colorValor: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, fontSize = 13.sp)
        Text(text = valor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = colorValor)
    }
}

@Composable
fun DialogoCombinarPedido(
    onDismiss: () -> Unit,
    onConfirmar: (List<Producto>, String?) -> Unit
) {
    var productoSeleccionadoIndex by remember { mutableIntStateOf(0) }
    var cantidadExtra by remember { mutableIntStateOf(1) }
    var notaExtra by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("➕ Combinar Pedido (Operador '+')") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Seleccione un producto para armar un pedido secundario y fusionarlo automáticamente mediante sobrecarga de operador:",
                    fontSize = 13.sp
                )

                CatalogoProductos.catalogo.forEachIndexed { index, producto ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (productoSeleccionadoIndex == index),
                            onClick = { productoSeleccionadoIndex = index }
                        )
                        Text(
                            text = "${producto.nombre} ($${"%.2f".format(producto.precio)})",
                            fontSize = 13.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Cantidad (1-100):")
                    IconButton(onClick = { if (cantidadExtra > 1) cantidadExtra-- }) {
                        Text("−", fontWeight = FontWeight.Bold)
                    }
                    Text("$cantidadExtra", fontWeight = FontWeight.Bold)
                    IconButton(onClick = { if (cantidadExtra < 100) cantidadExtra++ }) {
                        Text("+", fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedTextField(
                    value = notaExtra,
                    onValueChange = { notaExtra = it },
                    label = { Text("Nota de la adición (Opcional)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val productoElegido = CatalogoProductos.catalogo[productoSeleccionadoIndex]
                val productosAdicionales = List(cantidadExtra) { productoElegido }
                onConfirmar(productosAdicionales, notaExtra.ifBlank { null })
            }) {
                Text("Combinar con '+'")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
