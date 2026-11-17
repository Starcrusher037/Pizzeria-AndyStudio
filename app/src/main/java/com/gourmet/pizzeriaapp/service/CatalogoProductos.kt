package com.gourmet.pizzeriaapp.service

import com.gourmet.pizzeriaapp.model.ComboFamiliar
import com.gourmet.pizzeriaapp.model.Pizza
import com.gourmet.pizzeriaapp.model.PizzaPromocional
import com.gourmet.pizzeriaapp.model.Producto

object CatalogoProductos {
    val catalogo: List<Producto> = listOf(
        Pizza("Pizza Pepperoni", 8990.0, listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Pepperoni")),
        Pizza("Pizza Napolitana", 9490.0, listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Jamón")),
        Pizza("Pizza Vegetariana", 9990.0, listOf("Masa", "Salsa de Tomate", "Queso Mozzarella", "Champiñones")),
        PizzaPromocional(), // Anotado con @Promocionable (Paso 2)
        ComboFamiliar(),    // Anotado con @Promocionable (Paso 2)
        Producto("Coca Cola 1.5L", 2500.0, emptyList(), categoria = "Bebida")
    )
}
