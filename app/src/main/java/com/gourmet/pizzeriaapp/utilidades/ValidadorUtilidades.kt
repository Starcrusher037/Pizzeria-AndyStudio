package com.gourmet.pizzeriaapp.utilidades

/**
 * Objeto utilitario encargado de validaciones basadas en Expresiones Regulares (Regex)
 * y Verificaciones de Rangos (Ranges) segun el Paso 1.
 */
object ValidadorUtilidades {

    /**
     * Expresion regular estandar para validar direcciones de correo electronico (nombre@dominio.com).
     * Asegura la presencia de usuario, '@', subdominio y extension de al menos 2 letras.
     */
    val REGEX_EMAIL: Regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    /**
     * Rango permitido para la cantidad de productos en un pedido (Paso 1: Ranges 1..100).
     */
    val RANGO_CANTIDAD_PRODUCTOS: IntRange = 1..100

    /**
     * Valida si un texto cumple con el formato estandar de email mediante Regex.
     */
    fun validarEmail(email: String): Boolean {
        return REGEX_EMAIL.matches(email.trim())
    }

    /**
     * Valida si una cantidad entera se encuentra dentro del rango permitido (1..100).
     */
    fun validarCantidadEnRango(cantidad: Int): Boolean {
        return cantidad in RANGO_CANTIDAD_PRODUCTOS
    }

    /**
     * Valida si una cantidad ingresada como texto representa un entero dentro del rango 1..100.
     */
    fun validarCantidadTexto(texto: String): Boolean {
        val num = texto.trim().toIntOrNull() ?: return false
        return validarCantidadEnRango(num)
    }
}
