package com.gourmet.pizzeriaapp.model

class Repartidor(
    val idRepartidor: Int,
    nombre: String,
    telefono: String,
    email: String,
    val distanciaKm: Double,
    disponibleInicial: Boolean = true
) : Usuario(nombre, telefono, email) {

    // Encapsulamiento: solo modificable mediante los métodos de negocio de la clase
    var disponible: Boolean = disponibleInicial
        private set

    fun asignar() {
        this.disponible = false
    }

    fun liberar() {
        this.disponible = true
    }

    fun entregarPedido(pedido: Pedido): String {
        asignar()
        pedido.cambiarEstadoPedido("En camino")
        val msgSalida = "El repartidor $nombre ha salido con el pedido #${pedido.idPedido} (distancia: ${distanciaKm} km)."
        pedido.cambiarEstadoPedido("Listo")
        val msgEntrega = "¡Pedido #${pedido.idPedido} entregado con éxito por el repartidor $nombre!"
        liberar()
        return "$msgSalida\n$msgEntrega"
    }
}
