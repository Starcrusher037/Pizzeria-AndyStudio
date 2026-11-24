package com.gourmet.pizzeriaapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gourmet.pizzeriaapp.model.Cliente
import com.gourmet.pizzeriaapp.model.Pedido
import com.gourmet.pizzeriaapp.model.Producto
import com.gourmet.pizzeriaapp.model.Repartidor
import com.gourmet.pizzeriaapp.service.CatalogoProductos
import com.gourmet.pizzeriaapp.service.InventarioService
import com.gourmet.pizzeriaapp.service.PedidoService
import com.gourmet.pizzeriaapp.util.ValidadorEntrada
import com.gourmet.pizzeriaapp.utilidades.GestorRegistro
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class PizzeriaAppState : ViewModel() {

    var siguienteIdPedido by mutableIntStateOf(101)
        private set

    // Lista de pedidos del día
    val listaPedidos = mutableStateListOf<Pedido>()

    // Inventario observable
    val stock = mutableStateMapOf<String, Int>().apply {
        putAll(InventarioService.consultarStock())
    }

    // Repartidores
    val listaRepartidores = mutableStateListOf<Repartidor>().apply {
        addAll(PedidoService.obtenerRepartidores())
    }

    // Formulario de Nuevo Pedido
    var nombreCliente by mutableStateOf("")
    var telefonoCliente by mutableStateOf("")
    var emailCliente by mutableStateOf("")
    var direccionCliente by mutableStateOf("")
    var notasPedido by mutableStateOf("")

    // Productos agregados al pedido actual: Lista de Producto
    val productosEnCarrito = mutableStateListOf<Producto>()

    // Mensajes para SnackBar o Diálogos
    var mensajeNotificacion by mutableStateOf<String?>(null)
    var errorStockFaltante by mutableStateOf<List<String>?>(null)

    init {
        // Inicializar GestorRegistro con productos del catálogo
        for (prod in CatalogoProductos.catalogo) {
            GestorRegistro.registrarProducto(prod)
        }

        // Cargar un pedido de ejemplo inicial para que la vista de Reportes no esté vacía
        val clienteEjemplo = Cliente(
            nombre = "Martín Silva",
            telefono = "+56 (987) 654-3210",
            email = "martin.silva@email.com",
            direccion = "Av. Providencia 1234"
        )
        GestorRegistro.registrarCliente(clienteEjemplo)

        val pedidoEjemplo = Pedido(
            idPedido = siguienteIdPedido++,
            cliente = clienteEjemplo,
            productos = listOf(
                CatalogoProductos.catalogo[0], // Pepperoni
                CatalogoProductos.catalogo[3], // Pizza Promocional
                CatalogoProductos.catalogo[5]  // Coca Cola
            ),
            estadoInicial = "Listo",
            notas = "Dejar en conserjería",
            fecha = LocalDate.now()
        )
        listaPedidos.add(pedidoEjemplo)
    }

    fun sincronizarStock() {
        stock.clear()
        stock.putAll(InventarioService.consultarStock())
    }

    fun agregarProductoAlCarrito(producto: Producto, cantidad: Int) {
        if (cantidad in 1..100) {
            repeat(cantidad) {
                productosEnCarrito.add(producto)
            }
        }
    }

    fun removerProductoDelCarrito(producto: Producto) {
        val index = productosEnCarrito.indexOf(producto)
        if (index >= 0) {
            productosEnCarrito.removeAt(index)
        }
    }

    fun limpiarCarrito() {
        productosEnCarrito.clear()
        notasPedido = ""
    }

    fun limpiarFormularioCompleto() {
        nombreCliente = ""
        telefonoCliente = ""
        emailCliente = ""
        direccionCliente = ""
        limpiarCarrito()
    }

    /**
     * Calcula un pedido temporal previo a la confirmación para desglosar totales y descuentos.
     */
    fun obtenerPedidoPreview(): Pedido? {
        if (productosEnCarrito.isEmpty()) return null
        val clienteTemp = Cliente(
            nombre = if (nombreCliente.isBlank()) "Cliente" else nombreCliente,
            telefono = telefonoCliente.ifBlank { null },
            email = emailCliente.ifBlank { null },
            direccion = if (direccionCliente.isBlank()) "Local" else direccionCliente
        )
        return Pedido(
            idPedido = siguienteIdPedido,
            cliente = clienteTemp,
            productos = productosEnCarrito.toList(),
            estadoInicial = "En preparacion",
            notas = notasPedido.ifBlank { null },
            fecha = LocalDate.now()
        )
    }

    /**
     * Paso 3: Sobrecarga del operador '+' para combinar el pedido actual con una adición.
     */
    fun combinarPedidoConAdicion(productosAdicionales: List<Producto>, notasAdicionales: String?) {
        val pedidoActual = obtenerPedidoPreview() ?: return
        val pedidoAdicional = Pedido(
            idPedido = siguienteIdPedido++,
            cliente = pedidoActual.cliente,
            productos = productosAdicionales,
            estadoInicial = "En preparacion",
            notas = notasAdicionales,
            fecha = LocalDate.now()
        )

        // Ejecución de la sobrecarga del operador '+' (Paso 3)
        val combinado = pedidoActual + pedidoAdicional

        // Actualizamos el carrito y notas con el pedido combinado
        productosEnCarrito.clear()
        productosEnCarrito.addAll(combinado.productos)
        notasPedido = combinado.notas ?: ""
        mensajeNotificacion = "¡Pedidos combinados con '+' exitosamente! Total de productos: ${combinado.productos.size}"
    }

    /**
     * Procesa la confirmación del pedido:
     * - Valida campos con Regex y Rangos (Paso 1).
     * - Registra al cliente y verifica duplicados (Paso 5).
     * - Verifica y descuenta inventario (InventarioService).
     * - Asigna repartidor disponible según cercanía (PedidoService).
     * - Agrega el pedido y actualiza la recaudación.
     */
    fun confirmarPedido(): Boolean {
        // Validar nombre
        val valNombre = ValidadorEntrada.validarNombre(nombreCliente)
        if (valNombre.isFailure) {
            mensajeNotificacion = "Error en nombre: ${valNombre.exceptionOrNull()?.message}"
            return false
        }

        // Validar direccion
        val valDireccion = ValidadorEntrada.validarDireccion(direccionCliente)
        if (valDireccion.isFailure) {
            mensajeNotificacion = "Error en dirección: ${valDireccion.exceptionOrNull()?.message}"
            return false
        }

        // Validar telefono opcional
        val valTel = ValidadorEntrada.validarTelefono(telefonoCliente)
        if (valTel.isFailure) {
            mensajeNotificacion = "Error en teléfono: ${valTel.exceptionOrNull()?.message}"
            return false
        }

        // Validar email opcional
        val valEmail = ValidadorEntrada.validarEmail(emailCliente)
        if (valEmail.isFailure) {
            mensajeNotificacion = "Error en email: ${valEmail.exceptionOrNull()?.message}"
            return false
        }

        if (productosEnCarrito.isEmpty()) {
            mensajeNotificacion = "Debe agregar al menos un producto al pedido."
            return false
        }

        val nombreFinal = valNombre.getOrThrow()
        val dirFinal = valDireccion.getOrThrow()
        val telFinal = valTel.getOrNull()
        val emailFinal = valEmail.getOrNull()

        val nuevoCliente = Cliente(
            nombre = nombreFinal,
            telefono = telFinal,
            email = emailFinal,
            direccion = dirFinal
        )

        // Registrar cliente en GestorRegistro y evaluar duplicados
        val esClienteNuevo = GestorRegistro.registrarCliente(nuevoCliente)
        val avisoCliente = if (!esClienteNuevo) " (Cliente recurrente)" else " (Nuevo cliente)"

        // Validar stock en inventario
        val (disponible, faltantes) = InventarioService.verificarDisponibilidadDetalle(productosEnCarrito)
        if (!disponible) {
            errorStockFaltante = faltantes
            mensajeNotificacion = "No hay stock suficiente para procesar el pedido."
            return false
        }

        // Descontar inventario
        InventarioService.descontarIngredientesPedido(productosEnCarrito)
        sincronizarStock()

        // Crear pedido
        val pedidoCreado = Pedido(
            idPedido = siguienteIdPedido++,
            cliente = nuevoCliente,
            productos = productosEnCarrito.toList(),
            estadoInicial = "En preparacion",
            notas = notasPedido.ifBlank { null },
            fecha = LocalDate.now()
        )

        // Notificar cliente
        val msgNotif = PedidoService.notificarCliente(pedidoCreado)

        // Asignar repartidor
        val repartidor = PedidoService.asignarRepartidor(pedidoCreado)
        val msgRepartidor = if (repartidor != null) {
            "Repartidor: ${repartidor.nombre} (${repartidor.distanciaKm} km)"
        } else {
            "Sin repartidor asignado de momento"
        }

        listaPedidos.add(0, pedidoCreado)
        limpiarFormularioCompleto()

        mensajeNotificacion = "¡Pedido #${pedidoCreado.idPedido} registrado con éxito!$avisoCliente. $msgRepartidor. $msgNotif"
        return true
    }

    fun cambiarEstadoPedido(pedido: Pedido, nuevoEstado: String) {
        pedido.cambiarEstadoPedido(nuevoEstado)
        // Reemplazar en la lista observable para gatillar recomposición
        val index = listaPedidos.indexOf(pedido)
        if (index >= 0) {
            listaPedidos[index] = Pedido(
                idPedido = pedido.idPedido,
                cliente = pedido.cliente,
                productos = pedido.productos,
                estadoInicial = nuevoEstado,
                notas = pedido.notas,
                fecha = pedido.fecha
            )
        }
    }

    fun entregarPedidoConRepartidor(pedido: Pedido) {
        val repartidor = listaRepartidores.firstOrNull { it.disponible } ?: listaRepartidores.firstOrNull()
        if (repartidor != null) {
            val resultado = repartidor.entregarPedido(pedido)
            cambiarEstadoPedido(pedido, "Listo")
            mensajeNotificacion = resultado
        } else {
            cambiarEstadoPedido(pedido, "Listo")
            mensajeNotificacion = "Pedido #${pedido.idPedido} marcado como Listo."
        }
    }

    // Funciones de reabastecimiento: se mantienen para soporte del diálogo y para una futura implementación en un panel de administración/cocina
    fun reabastecerIngrediente(ingrediente: String, cantidad: Int = 10) {
        InventarioService.reabastecer(ingrediente, cantidad)
        sincronizarStock()
        mensajeNotificacion = "Reabastecido $ingrediente (+$cantidad unidades)."
    }

    fun reabastecerTodo() {
        InventarioService.reabastecerTodo(15)
        sincronizarStock()
        errorStockFaltante = null
        mensajeNotificacion = "Todos los ingredientes fueron reabastecidos con éxito (+15 cada uno)."
    }
}
