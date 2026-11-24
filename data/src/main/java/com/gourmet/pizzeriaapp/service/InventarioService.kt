package com.gourmet.pizzeriaapp.service

import com.gourmet.pizzeriaapp.model.Producto

/**
 * Servicio de Inventario:
 * Actualmente la interfaz de usuario de Inventario fue retirada del flujo del cliente.
 * Este servicio y su lógica de gestión de stock quedan activos en segundo plano y
 * preservados para una futura implementación (módulo administrativo/bodega).
 */
object InventarioService {

    // Encapsulado: el mapa mutable no puede ser alterado directamente desde el exterior
    private val stockIngredientes: MutableMap<String, Int> = mutableMapOf(
        // Ingredientes base clásicos
        "Masa" to 25,
        "Salsa de Tomate" to 25,
        "Queso Mozzarella" to 25,
        "Pepperoni" to 15,
        "Jamón" to 15,
        "Champiñones" to 15,
        // Ingredientes Pizzas Gourmet
        "Salsa San Marzano" to 25,
        "Mozzarella fior di latte" to 25,
        "Albahaca fresca" to 25,
        "Aceite de oliva" to 25,
        "Pepperoni artesanal" to 25,
        "Mozzarella fundida" to 25,
        "Chile quebrado" to 25,
        "Miel de abeja picante" to 25,
        "Mozzarella" to 25,
        "Gorgonzola dulce" to 25,
        "Parmesano reggiano" to 25,
        "Fontina" to 25,
        "Nuez tostada" to 25
    )

    // Consulta de solo lectura del stock (retorna copia inmutable)
    fun consultarStock(): Map<String, Int> = stockIngredientes.toMap()

    fun reabastecer(ingrediente: String, cantidad: Int = 10) {
        stockIngredientes[ingrediente] = (stockIngredientes[ingrediente] ?: 0) + cantidad
    }

    fun reabastecerTodo(cantidadPorIngrediente: Int = 15) {
        stockIngredientes.keys.toList().forEach {
            stockIngredientes[it] = (stockIngredientes[it] ?: 0) + cantidadPorIngrediente
        }
    }

    fun verificarDisponibilidadDetalle(productos: List<Producto>): Pair<Boolean, List<String>> {
        val requeridos = calcularIngredientesRequeridos(productos)
        val faltantes = mutableListOf<String>()

        for ((ingrediente, cantidadRequerida) in requeridos) {
            val disponible = stockIngredientes.getOrPut(ingrediente) { 20 }
            if (disponible < cantidadRequerida) {
                faltantes.add("$ingrediente (Requerido: $cantidadRequerida, Disponible: $disponible)")
            }
        }
        return (faltantes.isEmpty()) to faltantes
    }

    fun verificarDisponibilidad(productos: List<Producto>): Boolean {
        return verificarDisponibilidadDetalle(productos).first
    }

    // Método auxiliar privado: lógica interna encapsulada
    private fun calcularIngredientesRequeridos(productos: List<Producto>): Map<String, Int> {
        val requeridos = mutableMapOf<String, Int>()
        for (producto in productos) {
            for (ingrediente in producto.ingredientes) {
                requeridos[ingrediente] = (requeridos[ingrediente] ?: 0) + 1
            }
        }
        return requeridos
    }

    // Método auxiliar privado: solo accesible y ejecutable por el servicio
    private fun descontarLoteIngredientes(ingredientes: List<String>) {
        for (ingrediente in ingredientes) {
            val stockActual = stockIngredientes[ingrediente] ?: 0
            if (stockActual > 0) {
                stockIngredientes[ingrediente] = stockActual - 1
            }
        }
    }

    fun descontarIngredientesPedido(productos: List<Producto>) {
        for (producto in productos) {
            descontarLoteIngredientes(producto.ingredientes)
        }
    }
}
