package com.gourmet.pizzeriaapp.util

import com.gourmet.pizzeriaapp.utilidades.FormatoUtilidades
import com.gourmet.pizzeriaapp.utilidades.ValidadorUtilidades

/**
 * Objeto utilitario encargado de gestionar y validar las entradas de datos.
 * Integra validaciones avanzadas con Regex y Rangos (Ranges).
 */
object ValidadorEntrada {

    val regexNombre = Regex("^[\\p{L}]+([\\p{L} '\\-]*[\\p{L}]+)?$")
    val regexDireccion = Regex("^[\\p{L}0-9][\\p{L}0-9 .,'°#/\\-]*$")

    fun validarNombre(nombre: String): Result<String> {
        val limpio = nombre.trim()
        if (limpio.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre no puede estar vacío."))
        }
        if (!regexNombre.matches(limpio)) {
            return Result.failure(
                IllegalArgumentException("El nombre solo puede contener letras y espacios.")
            )
        }
        return Result.success(limpio)
    }

    fun validarDireccion(direccion: String): Result<String> {
        val limpio = direccion.trim()
        if (limpio.isBlank()) {
            return Result.failure(IllegalArgumentException("La dirección no puede estar vacía."))
        }
        if (!regexDireccion.matches(limpio)) {
            return Result.failure(
                IllegalArgumentException("La dirección debe contener un nombre de calle y numeración válidos.")
            )
        }
        return Result.success(limpio)
    }

    fun validarTelefono(telefono: String?): Result<String?> {
        if (telefono.isNullOrBlank()) {
            return Result.success(null)
        }
        val soloDigitos = telefono.replace(Regex("\\D"), "")
        if (soloDigitos.length !in 8..12) {
            return Result.failure(IllegalArgumentException("El teléfono debe contener entre 8 y 12 dígitos numéricos."))
        }
        return Result.success(FormatoUtilidades.formatearTelefono(telefono))
    }

    fun validarEmail(email: String?): Result<String?> {
        if (email.isNullOrBlank()) {
            return Result.success(null)
        }
        if (!ValidadorUtilidades.validarEmail(email)) {
            return Result.failure(IllegalArgumentException("Formato de correo electrónico inválido (ej: nombre@dominio.com)."))
        }
        return Result.success(email.trim())
    }

    fun validarCantidad(cantidad: Int): Result<Int> {
        if (!ValidadorUtilidades.validarCantidadEnRango(cantidad)) {
            return Result.failure(IllegalArgumentException("La cantidad debe estar entre ${ValidadorUtilidades.RANGO_CANTIDAD_PRODUCTOS.first} y ${ValidadorUtilidades.RANGO_CANTIDAD_PRODUCTOS.last}."))
        }
        return Result.success(cantidad)
    }
}
