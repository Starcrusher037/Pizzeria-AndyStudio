package com.gourmet.pizzeriaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gourmet.pizzeriaapp.model.*
import com.gourmet.pizzeriaapp.service.CatalogoProductos
import com.gourmet.pizzeriaapp.util.ValidadorEntrada
import com.gourmet.pizzeriaapp.utilidades.AnalizadorReflection
import com.gourmet.pizzeriaapp.utilidades.FormatoUtilidades
import com.gourmet.pizzeriaapp.utilidades.GestorRegistro
import com.gourmet.pizzeriaapp.utilidades.PromocionFechaHelper
import com.gourmet.pizzeriaapp.utilidades.ValidadorUtilidades
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaboratorioScreen() {
    var pasoSeleccionado by remember { mutableIntStateOf(1) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Laboratorio de Conceptos Técnicos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Demostración interactiva de los 5 pilares funcionales migrados desde PizzeriaS1.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        // TABS DE LOS 5 PASOS
        item {
            val titulos = listOf(
                "Paso 1: Ranges & Regex",
                "Paso 2: Reflection",
                "Paso 3: Operadores",
                "Paso 4: Desestructuración",
                "Paso 5: Duplicados"
            )
            PrimaryScrollableTabRow(
                selectedTabIndex = pasoSeleccionado - 1,
                edgePadding = 0.dp
            ) {
                titulos.forEachIndexed { index, titulo ->
                    Tab(
                        selected = (pasoSeleccionado == index + 1),
                        onClick = { pasoSeleccionado = index + 1 },
                        text = { Text(titulo, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }
        }

        // CONTENIDO SEGÚN PASO
        item {
            when (pasoSeleccionado) {
                1 -> SeccionPaso1RangesRegex()
                2 -> SeccionPaso2Reflection()
                3 -> SeccionPaso3Operadores()
                4 -> SeccionPaso4Desestructuracion()
                5 -> SeccionPaso5Duplicados()
            }
        }
    }
}

// -----------------------------------------------------------------------------
// PASO 1: RANGOS Y REGEX
// -----------------------------------------------------------------------------
@Composable
fun SeccionPaso1RangesRegex() {
    var emailPrueba by remember { mutableStateOf("cliente@pizzeria.cl") }
    var telefonoPrueba by remember { mutableStateOf("98765432") }
    var cantidadPrueba by remember { mutableStateOf("4") }

    Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Paso 1: Ranges (Rangos) y Expresiones Regulares (Regex)",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            // 1. Rango de Fechas
            val hoy = LocalDate.now()
            val enPeriodo = PromocionFechaHelper.estaEnPeriodoPromocional(hoy)
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("1. ClosedRange<LocalDate> (rangeTo '..' e 'in'):", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("Rango promocional: ${PromocionFechaHelper.PERIODO_PROMOCIONAL.start} .. ${PromocionFechaHelper.PERIODO_PROMOCIONAL.endInclusive}", fontSize = 12.sp)
                    Text("Fecha actual: $hoy", fontSize = 12.sp)
                    Text("Resultado 'hoy in PERIODO_PROMOCIONAL': ${if (enPeriodo) "VERDADERO (10% descuento)" else "FALSO"}",
                        color = if (enPeriodo) Color(0xFF2E7D32) else Color(0xFFC62828), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            // 2. Rango de Cantidades 1..100
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("2. IntRange para cantidad (1..100):", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    OutlinedTextField(
                        value = cantidadPrueba,
                        onValueChange = { cantidadPrueba = it },
                        label = { Text("Probar cantidad") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    val cantInt = cantidadPrueba.toIntOrNull()
                    val cantValida = cantInt != null && ValidadorUtilidades.validarCantidadEnRango(cantInt)
                    Text(
                        text = if (cantValida) "Cantidad $cantInt es válida en 1..100" else "Cantidad inválida (debe ser entero entre 1 y 100)",
                        color = if (cantValida) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 3. Regex Email
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("3. Regex Email (nombre@dominio.com):", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    OutlinedTextField(
                        value = emailPrueba,
                        onValueChange = { emailPrueba = it },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    val emailValido = ValidadorUtilidades.validarEmail(emailPrueba)
                    Text(
                        text = if (emailValido) "Correo con formato válido" else "Formato inválido según REGEX_EMAIL",
                        color = if (emailValido) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 4. Regex Teléfono con grupos de captura
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("4. Regex Formateador Telefónico (+XX (XXX) XXX-XXXX):", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    OutlinedTextField(
                        value = telefonoPrueba,
                        onValueChange = { telefonoPrueba = it },
                        label = { Text("Número telefónico") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    val formateado = FormatoUtilidades.formatearTelefono(telefonoPrueba)
                    Text(
                        text = "Formato uniforme generado: $formateado",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// PASO 2: ANOTACIONES Y REFLECTION
// -----------------------------------------------------------------------------
@Composable
fun SeccionPaso2Reflection() {
    val clases = listOf(
        "PizzaPromocional" to PizzaPromocional::class.java,
        "ComboFamiliar" to ComboFamiliar::class.java,
        "Pizza (Estándar)" to Pizza::class.java,
        "Cliente" to Cliente::class.java,
        "Pedido" to Pedido::class.java
    )
    var indexSeleccionado by remember { mutableIntStateOf(0) }
    val info = remember(indexSeleccionado) {
        AnalizadorReflection.obtenerInformacionClase(clases[indexSeleccionado].second)
    }

    Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Paso 2: Anotaciones (@Promocionable) y Reflection",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text("Seleccione la clase para inspeccionar en tiempo de ejecución:", fontSize = 12.sp)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                clases.forEachIndexed { i, (nombre, _) ->
                    FilterChip(
                        selected = (indexSeleccionado == i),
                        onClick = { indexSeleccionado = i },
                        label = { Text(nombre, fontSize = 11.sp) }
                    )
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Entidad: ${info.nombreSimple} (${info.nombreCompleto})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    HorizontalDivider()

                    Text("Anotaciones detectadas (${info.anotaciones.size}):", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    if (info.anotaciones.isEmpty()) {
                        Text("  (Sin anotaciones personalizadas a nivel de clase)", fontSize = 11.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    } else {
                        info.anotaciones.forEach {
                            Text("  • $it", fontSize = 11.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                        }
                    }

                    Text("Propiedades y campos (${info.propiedades.size}):", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    info.propiedades.take(8).forEach { (prop, tipo) ->
                        Text("  • $prop: $tipo", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                    if (info.propiedades.size > 8) {
                        Text("  ... (${info.propiedades.size - 8} más)", fontSize = 10.sp)
                    }

                    Text("Métodos declarados (${info.metodos.size}):", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    info.metodos.take(6).forEach { (metodo, retorno) ->
                        Text("  • $metodo -> $retorno", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// PASO 3: SOBRECARGA DE OPERADORES (+ y ==)
// -----------------------------------------------------------------------------
@Composable
fun SeccionPaso3Operadores() {
    var resultadoCombinacion by remember { mutableStateOf<String?>(null) }
    var resultadoIgualdad by remember { mutableStateOf<String?>(null) }

    Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Paso 3: Sobrecarga de Operadores ('+' y '==')",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            // 1. Operador '+' en Pedido
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("1. Sobrecarga del operador '+' (Pedido + Pedido):", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("Combina dos instancias de Pedido sumando productos, concatenando notas y recalculando totales.", fontSize = 12.sp)

                    Button(
                        onClick = {
                            val clienteA = Cliente("Carlos", "999111", "carlos@test.com", "Calle A 100")
                            val ped1 = Pedido(101, clienteA, listOf(CatalogoProductos.catalogo[0]), notas = "Sin picante")
                            val ped2 = Pedido(102, clienteA, listOf(CatalogoProductos.catalogo[5]), notas = "Bien fría")

                            val combinado = ped1 + ped2

                            resultadoCombinacion = """
                                Pedido 1: #${ped1.idPedido} (${ped1.productos.size} prod, notas: '${ped1.notas}')
                                Pedido 2: #${ped2.idPedido} (${ped2.productos.size} prod, notas: '${ped2.notas}')
                                --- EJECUTANDO 'ped1 + ped2' ---
                                Pedido Combinado: #${combinado.idPedido}
                                Total productos: ${combinado.productos.size}
                                Notas combinadas: '${combinado.notas}'
                                Total recalculado: $${"%.2f".format(combinado.calcularTotal())}
                            """.trimIndent()
                        }
                    ) {
                        Text("Ejecutar 'ped1 + ped2'")
                    }

                    if (resultadoCombinacion != null) {
                        Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(6.dp)) {
                            Text(
                                text = resultadoCombinacion!!,
                                modifier = Modifier.padding(8.dp),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // 2. Operador '==' en Producto
            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("2. Sobrecarga del operador '==' (equals() en Producto y Cliente):", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("Compara por atributos clave ignorando mayúsculas y precio:", fontSize = 12.sp)

                    Button(
                        onClick = {
                            val p1 = Producto("Pizza Napolitana", 9490.0, categoria = "Pizza")
                            val p2 = Producto("PIZZA NAPOLITANA", 9990.0, categoria = "Pizza") // Precio distinto, mayúsculas
                            val sonIguales = (p1 == p2)

                            resultadoIgualdad = """
                                p1 = Producto("Pizza Napolitana", $9490, cat="Pizza")
                                p2 = Producto("PIZZA NAPOLITANA", $9990, cat="Pizza")
                                Evaluación 'p1 == p2': $sonIguales
                                Razón: Coinciden en nombre y categoría (ignoreCase = true).
                            """.trimIndent()
                        }
                    ) {
                        Text("Evaluar 'p1 == p2'")
                    }

                    if (resultadoIgualdad != null) {
                        Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(6.dp)) {
                            Text(
                                text = resultadoIgualdad!!,
                                modifier = Modifier.padding(8.dp),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// PASO 4: DESESTRUCTURACIÓN
// -----------------------------------------------------------------------------
@Composable
fun SeccionPaso4Desestructuracion() {
    Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Paso 4: Declaraciones de Desestructuración",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            // Ejemplo en Cliente
            val cliente = Cliente("Valeria Morales", "+56 (987) 123-4567", "valeria@gmail.com", "Av. Brasil 450")
            val (nom, correo, fono) = cliente // component1(), component2(), component3()

            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("1. Desestructuración en Cliente:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("val (nombre, email, telefono) = cliente", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    Text("• nombre (component1): $nom", fontSize = 12.sp)
                    Text("• email (component2): $correo", fontSize = 12.sp)
                    Text("• telefono (component3): $fono", fontSize = 12.sp)
                }
            }

            // Ejemplo en Pedido
            val pedido = Pedido(105, cliente, listOf(CatalogoProductos.catalogo[0], CatalogoProductos.catalogo[1]))
            val (cli, prods, total) = pedido // component1(), component2(), component3()

            Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("2. Desestructuración en Pedido:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("val (cliente, productos, total) = pedido", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    Text("• cliente (component1): ${cli.nombre}", fontSize = 12.sp)
                    Text("• productos (component2): ${prods.size} item(s)", fontSize = 12.sp)
                    Text("• total (component3): $${"%.2f".format(total)}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Ejemplo en Regex
            val match = Regex("^(\\d{2})(\\d{3})(\\d{3})(\\d{4})$").matchEntire("569876543210")
            if (match != null) {
                val (pais, area, prefijo, linea) = match.destructured
                Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("3. Desestructuración en Regex Match:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("val (pais, area, prefijo, linea) = match.destructured", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                        Text("• país: +$pais | área: ($area) | prefijo: $prefijo | línea: $linea", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// PASO 5: EVALUACIÓN DE IGUALDAD Y PREVENCIÓN DE DUPLICADOS
// -----------------------------------------------------------------------------
@Composable
fun SeccionPaso5Duplicados() {
    var registroLog by remember { mutableStateOf<List<String>>(emptyList()) }

    Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Paso 5: Evaluación de Igualdad y Detección de Duplicados",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "GestorRegistro utiliza colecciones Set junto con sobreescritura coherente de equals() y hashCode() para garantizar que no existan entidades duplicadas.",
                fontSize = 12.sp
            )

            Button(
                onClick = {
                    val logs = mutableListOf<String>()
                    val c1 = Cliente("Lucía Gómez", "9876", "lucia@test.com", "Calle 1")
                    val c2 = Cliente("LUCÍA GÓMEZ", "5555", "lucia@test.com", "Calle Otra") // Mismo nombre y email

                    val reg1 = GestorRegistro.registrarCliente(c1)
                    logs.add("1. Registro c1 ('Lucía Gómez'): ${if (reg1) "EXITOSO (Nuevo)" else "DUPLICADO DETECTADO"}")

                    val reg2 = GestorRegistro.registrarCliente(c2)
                    logs.add("2. Registro c2 ('LUCÍA GÓMEZ'): ${if (reg2) "EXITOSO (Nuevo)" else "RECHAZADO (Duplicado por equals/hashCode)"}")

                    val p1 = Producto("Pizza Margarita", 8990.0, categoria = "Pizza")
                    val p2 = Producto("PIZZA MARGARITA", 9990.0, categoria = "Pizza")

                    val regP1 = GestorRegistro.registrarProducto(p1)
                    logs.add("3. Registro p1 ('Pizza Margarita'): ${if (regP1) "EXITOSO (Nuevo)" else "DUPLICADO DETECTADO"}")

                    val regP2 = GestorRegistro.registrarProducto(p2)
                    logs.add("4. Registro p2 ('PIZZA MARGARITA'): ${if (regP2) "EXITOSO (Nuevo)" else "RECHAZADO (Duplicado por equals/hashCode)"}")

                    registroLog = logs
                }
            ) {
                Text("Probar Detección de Duplicados en GestorRegistro")
            }

            if (registroLog.isNotEmpty()) {
                Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        registroLog.forEach {
                            Text(text = it, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }
    }
}
