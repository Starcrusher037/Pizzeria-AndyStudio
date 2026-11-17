package com.gourmet.pizzeriaapp.utilidades

import com.gourmet.pizzeriaapp.model.Cliente
import com.gourmet.pizzeriaapp.model.Producto

/**
 * Gestor encargado de almacenar y administrar el registro de clientes y productos.
 * Utiliza las evaluaciones de igualdad de objetos (equals() y hashCode()) para evitar
 * el ingreso de duplicados en la aplicacion (Paso 5).
 */
object GestorRegistro {

    // Colecciones de tipo Set para garantizar unicidad automatica mediante equals y hashCode
    private val clientesRegistrados = mutableSetOf<Cliente>()
    private val productosRegistrados = mutableSetOf<Producto>()

    /**
     * Registra un nuevo cliente evaluando previamente si ya existe.
     * La comprobacion 'contains' utiliza la sobreescritura de equals() (nombre y correo).
     */
    fun registrarCliente(cliente: Cliente): Boolean {
        if (clientesRegistrados.contains(cliente)) {
            return false
        }
        clientesRegistrados.add(cliente)
        return true
    }

    /**
     * Registra un nuevo producto en el catalogo evaluando previamente si ya existe.
     * La comprobacion 'contains' utiliza la sobreescritura de equals() (nombre y categoria).
     */
    fun registrarProducto(producto: Producto): Boolean {
        if (productosRegistrados.contains(producto)) {
            return false
        }
        productosRegistrados.add(producto)
        return true
    }

    fun obtenerClientes(): Set<Cliente> = clientesRegistrados.toSet()

    fun obtenerProductos(): Set<Producto> = productosRegistrados.toSet()

    fun limpiar() {
        clientesRegistrados.clear()
        productosRegistrados.clear()
    }
}
