package com.gourmet.pizzeriaapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
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
import com.gourmet.pizzeriaapp.ui.PizzeriaAppState
import com.gourmet.pizzeriaapp.ui.theme.*
import com.gourmet.pizzeriaapp.utilidades.FormatoUtilidades

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartCheckoutSheet(
    appState: PizzeriaAppState,
    onDismiss: () -> Unit,
    onPedidoConfirmado: () -> Unit
) {
    val items = appState.productosEnCarrito
    // Agrupar items por nombre para visualización clara
    val itemsAgrupados = remember(items.toList()) {
        items.groupBy { it.nombre }
    }
    val pedidoPreview = appState.obtenerPedidoPreview()
    var pasoCheckout by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCard,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header del carrito
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (pasoCheckout) "Datos de Entrega" else "Tu Carrito",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Tu carrito está vacío",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Agrega deliciosas pizzas desde el menú",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }
                }
            } else if (!pasoCheckout) {
                // Lista de productos en carrito
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(itemsAgrupados.entries.toList()) { entry ->
                        val primerProducto = entry.value.first()
                        val cantidad = entry.value.size
                        val totalProducto = primerProducto.precio * cantidad

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = SurfaceBeigeLight,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = entry.key,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${FormatoUtilidades.formatearCLP(primerProducto.precio, incluirMoneda = true)} c/u",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Botón menos
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(SurfaceBeige)
                                            .clickable { appState.removerProductoDelCarrito(primerProducto) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Remove,
                                            contentDescription = "Quitar",
                                            modifier = Modifier.size(16.dp),
                                            tint = TextPrimary
                                        )
                                    }

                                    Text(
                                        text = "$cantidad",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = TextPrimary
                                    )

                                    // Botón más
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(PizzaRed)
                                            .clickable { appState.agregarProductoAlCarrito(primerProducto, 1) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Agregar",
                                            modifier = Modifier.size(16.dp),
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Resumen de totales
                if (pedidoPreview != null) {
                    val subtotal = pedidoPreview.calcularSubtotal()
                    val descuento = pedidoPreview.calcularDescuento()
                    val total = pedidoPreview.calcularTotal()

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceBeigeLight,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Subtotal:", fontSize = 13.sp, color = TextSecondary)
                                Text(FormatoUtilidades.formatearCLP(subtotal, incluirMoneda = true), fontSize = 13.sp, color = TextPrimary)
                            }
                            if (descuento > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Descuentos aplicados:", fontSize = 13.sp, color = FreshGreen)
                                    Text("-${FormatoUtilidades.formatearCLP(descuento, incluirMoneda = true)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FreshGreen)
                                }
                            }
                            HorizontalDivider(color = SurfaceBeige)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total a pagar:", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(
                                    FormatoUtilidades.formatearCLP(total, incluirMoneda = true),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PizzaRed
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { pasoCheckout = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PizzaRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Continuar con el pedido",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            } else {
                // Formulario de datos de cliente para confirmar
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = appState.nombreCliente,
                        onValueChange = { appState.nombreCliente = it },
                        label = { Text("Tu Nombre Completo *") },
                        placeholder = { Text("Ej: Valentina Carrasco") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PizzaRed,
                            unfocusedBorderColor = SurfaceBeige
                        )
                    )

                    OutlinedTextField(
                        value = appState.direccionCliente,
                        onValueChange = { appState.direccionCliente = it },
                        label = { Text("Dirección de Entrega *") },
                        placeholder = { Text("Av. Providencia 1234, Providencia") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PizzaRed,
                            unfocusedBorderColor = SurfaceBeige
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = appState.telefonoCliente,
                            onValueChange = { appState.telefonoCliente = it },
                            label = { Text("Teléfono") },
                            placeholder = { Text("+56 9 8765 4321") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PizzaRed,
                                unfocusedBorderColor = SurfaceBeige
                            )
                        )
                        OutlinedTextField(
                            value = appState.emailCliente,
                            onValueChange = { appState.emailCliente = it },
                            label = { Text("Correo") },
                            placeholder = { Text("cliente@email.com") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PizzaRed,
                                unfocusedBorderColor = SurfaceBeige
                            )
                        )
                    }

                    OutlinedTextField(
                        value = appState.notasPedido,
                        onValueChange = { appState.notasPedido = it },
                        label = { Text("Notas de entrega (opcional)") },
                        placeholder = { Text("Ej: Timbre 4B, masa bien dorada") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PizzaRed,
                            unfocusedBorderColor = SurfaceBeige
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { pasoCheckout = false },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Volver", color = TextSecondary)
                        }

                        Button(
                            onClick = {
                                val exito = appState.confirmarPedido()
                                if (exito) {
                                    onPedidoConfirmado()
                                }
                            },
                            modifier = Modifier
                                .weight(2f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PizzaRed),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Confirmar Pedido",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
