package com.gourmet.pizzeriaapp.uimodule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado inmutable del resumen del sistema.
 *
 * Encapsula los datos que se muestran dinamicamente en la pantalla inicial:
 * - totalClientes: cantidad de clientes registrados en el sistema.
 * - totalPedidos: cantidad de pedidos realizados en la sesion actual.
 * - ultimoCliente: nombre del ultimo cliente registrado, o null si no hay ninguno.
 */
data class ResumenEstado(
    val totalClientes: Int = 0,
    val totalPedidos: Int = 0,
    val ultimoCliente: String? = null,
    val cargando: Boolean = false,
    val mensajeProgreso: String = "Cargando datos..."
)

/**
 * ViewModel del modulo UI.
 *
 * Gestiona el estado del resumen de la aplicacion utilizando StateFlow (Kotlin Coroutines).
 * Actua como intermediario entre la logica de negocio (modulo :data) y las pantallas
 * Jetpack Compose, siguiendo el patron MVVM.
 *
 * El uso de viewModelScope garantiza que las coroutines se cancelan automaticamente
 * cuando el ViewModel es destruido, evitando fugas de memoria.
 */
class PizzeriaViewModel : ViewModel() {

    private val _resumen = MutableStateFlow(ResumenEstado())

    /**
     * StateFlow expuesto de forma de solo lectura hacia la capa de UI.
     * Las pantallas Compose se suscriben a este flujo y se recomponen automaticamente
     * cuando el estado cambia.
     */
    val resumen: StateFlow<ResumenEstado> = _resumen.asStateFlow()

    /**
     * Actualiza el estado del resumen con los datos mas recientes del sistema.
     * Se ejecuta en una coroutine dentro del scope del ViewModel para garantizar
     * que la operacion sea asincronica y no bloquee el hilo principal.
     *
     * @param totalClientes Numero total de clientes registrados.
     * @param totalPedidos  Numero total de pedidos realizados.
     * @param ultimoCliente Nombre del ultimo cliente registrado.
     */
    fun actualizarResumen(
        totalClientes: Int,
        totalPedidos: Int,
        ultimoCliente: String?,
        conSimulacionCarga: Boolean = false
    ) {
        viewModelScope.launch {
            if (conSimulacionCarga) {
                _resumen.update { it.copy(cargando = true, mensajeProgreso = "Cargando datos desde el sistema...") }
                kotlinx.coroutines.delay(500)
                _resumen.update { it.copy(mensajeProgreso = "Generando resumen...") }
                kotlinx.coroutines.delay(400)
            }
            _resumen.update { estadoActual ->
                estadoActual.copy(
                    totalClientes = totalClientes,
                    totalPedidos = totalPedidos,
                    ultimoCliente = ultimoCliente,
                    cargando = false,
                    mensajeProgreso = ""
                )
            }
        }
    }

    /**
     * Marca el estado como "cargando" con mensaje personalizado mientras se obtienen datos.
     * Utiliza coroutines para ejecutar la operacion de forma asincronica.
     */
    fun iniciarCarga(mensaje: String = "Cargando datos...") {
        viewModelScope.launch {
            _resumen.update { it.copy(cargando = true, mensajeProgreso = mensaje) }
        }
    }
}
