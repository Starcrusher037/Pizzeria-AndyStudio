package com.gourmet.pizzeriaapp.utilidades

import kotlin.reflect.KClass

/**
 * Objeto utilitario encargado de analizar e inspeccionar dinamicamente clases en tiempo de ejecucion
 * mediante mecanismos de Reflection (Paso 2: Reflection).
 *
 * Permite listar todas las propiedades, metodos y anotaciones de entidades clave
 * como Pedido, Producto o Cliente.
 */
object AnalizadorReflection {

    data class InformacionClase(
        val nombreSimple: String,
        val nombreCompleto: String,
        val anotaciones: List<String>,
        val propiedades: List<Pair<String, String>>,
        val metodos: List<Pair<String, String>>
    )

    /**
     * Inspecciona una clase Java/Kotlin utilizando Reflection y presenta un analisis estructurado:
     * 1. Anotaciones aplicadas a la clase (como @Promocionable).
     * 2. Propiedades y campos declarados (nombre y tipo).
     * 3. Metodos declarados (nombre, tipos de parametros y tipo de retorno).
     */
    fun obtenerInformacionClase(clase: Class<*>): InformacionClase {
        val anotacionesPersonalizadas = clase.annotations
            .filter { it.annotationClass.simpleName != "Metadata" }
            .map { "@${it.annotationClass.simpleName} ($it)" }

        val campos = clase.declaredFields
            .filter { !it.isSynthetic }
            .map { it.name to it.type.simpleName }

        val metodos = clase.declaredMethods
            .filter { !it.isSynthetic }
            .map { metodo ->
                val params = metodo.parameterTypes.joinToString(", ") { it.simpleName }
                val textoParams = if (params.isEmpty()) "sin parametros" else params
                "${metodo.name}($textoParams)" to metodo.returnType.simpleName
            }

        return InformacionClase(
            nombreSimple = clase.simpleName,
            nombreCompleto = clase.name,
            anotaciones = anotacionesPersonalizadas,
            propiedades = campos,
            metodos = metodos
        )
    }

    fun inspeccionarClase(clase: Class<*>): String {
        val info = obtenerInformacionClase(clase)
        return buildString {
            appendLine("------------------------------------------------------------------")
            appendLine("INSPECCION DINAMICA (REFLECTION): Entidad '${info.nombreSimple}'")
            appendLine("Nombre completo: ${info.nombreCompleto}")
            appendLine("------------------------------------------------------------------")
            appendLine("[Anotaciones a nivel de clase]")
            if (info.anotaciones.isEmpty()) {
                appendLine("  (Sin anotaciones directas a nivel de clase)")
            } else {
                info.anotaciones.forEach { appendLine("  - $it") }
            }
            appendLine("\n[Propiedades y Campos Declarados]")
            if (info.propiedades.isEmpty()) {
                appendLine("  (No se encontraron campos declarados)")
            } else {
                info.propiedades.forEach { appendLine("  - Propiedad: '${it.first}' | Tipo: ${it.second}") }
            }
            appendLine("\n[Metodos Declarados]")
            if (info.metodos.isEmpty()) {
                appendLine("  (No se encontraron metodos declarados)")
            } else {
                info.metodos.forEach { appendLine("  - Metodo: ${it.first} -> Retorno: ${it.second}") }
            }
            appendLine("------------------------------------------------------------------")
        }
    }

    /**
     * Sobrecarga que permite invocar la inspeccion pasando una referencia KClass de Kotlin (ej. Pedido::class).
     */
    fun inspeccionarClase(kClass: KClass<*>): String {
        return inspeccionarClase(kClass.java)
    }
}
