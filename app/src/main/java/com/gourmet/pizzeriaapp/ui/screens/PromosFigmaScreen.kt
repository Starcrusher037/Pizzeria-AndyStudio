package com.gourmet.pizzeriaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gourmet.pizzeriaapp.model.ComboFamiliar
import com.gourmet.pizzeriaapp.model.Pizza
import com.gourmet.pizzeriaapp.model.PizzaPromocional
import com.gourmet.pizzeriaapp.ui.PizzeriaAppState
import com.gourmet.pizzeriaapp.ui.theme.*

data class PromoFigmaItem(
    val id: String,
    val categoria: String,
    val titulo: String,
    val descripcion: String,
    val vigencia: String,
    val badge1Texto: String,
    val badge1Color: Color,
    val badge2Texto: String?,
    val badge2Color: Color?,
    val badgeInferiorTexto: String,
    val imageUrl: String,
    val precioPromo: Double
)

@Composable
fun PromosFigmaScreen(
    appState: PizzeriaAppState,
    onNavigateToNuevoPedido: () -> Unit
) {
    var categoriaFiltro by remember { mutableStateOf("Todas") }

    val promos = listOf(
        PromoFigmaItem(
            id = "martes2x1",
            categoria = "Días especiales",
            titulo = "2x1 en Martes de Pizza",
            descripcion = "Pide cualquier pizza mediana o grande de la carta y llévate la segunda de igual o menor valor totalmente gratis.",
            vigencia = "Vigencia: 30 Jun 2025",
            badge1Texto = "2x1 HOY",
            badge1Color = PizzaRed,
            badge2Texto = null,
            badge2Color = null,
            badgeInferiorTexto = "Válido todos los martes",
            imageUrl = "https://images.unsplash.com/photo-1579751626657-72bc17010498?w=800&q=80",
            precioPromo = 10490.0
        ),
        PromoFigmaItem(
            id = "combofamiliar",
            categoria = "Combos",
            titulo = "Combo Familiar: 2 Pizzas Grandes + 1 Refresco 2L + Ajo Pan",
            descripcion = "Arma la reunión perfecta con tus dos especialidades favoritas recién horneadas, pan de ajo artesanal y refresco bien frío.",
            vigencia = "Vigencia: Todos los días",
            badge1Texto = "AHORRA $5.000",
            badge1Color = PizzaRed,
            badge2Texto = "COMBO FAMILIAR",
            badge2Color = FreshGreen,
            badgeInferiorTexto = "Ideal para 4–5 personas",
            imageUrl = "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=800&q=80",
            precioPromo = 18990.0
        ),
        PromoFigmaItem(
            id = "miercolesalbahaca",
            categoria = "Vegetarianas",
            titulo = "Miércoles de Albahaca: 20% dto. en Pizzas Vegetarianas",
            descripcion = "Disfruta ingredientes frescos de la huerta, albahaca aromática y queso artesanal con un descuento especial cada miércoles.",
            vigencia = "Vigencia: 31 Jul 2025",
            badge1Texto = "20% DTO.",
            badge1Color = FreshGreen,
            badge2Texto = "100% Vegetariana",
            badge2Color = FreshGreen,
            badgeInferiorTexto = "Solo miércoles",
            imageUrl = "https://images.unsplash.com/photo-1604382355076-af4b0eb60143?w=800&q=80",
            precioPromo = 8990.0
        )
    )

    val promosFiltradas = if (categoriaFiltro == "Todas") promos else promos.filter { it.categoria == categoriaFiltro }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp)
    ) {
        // HEADER BANNER "Promos que enamoran"
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = PizzaRedLight,
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Whatshot,
                                    contentDescription = null,
                                    tint = PizzaRedDark,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Recién salidas del horno",
                                    color = PizzaRedDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = "Promos que enamoran",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = "Elige tu favorita y aplícala directo en tu próximo pedido.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(PizzaRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = "Regalo",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // CHIPS DE FILTRO (Todas, Combos, Días especiales, Vegetarianas)
        item {
            val filtros = listOf("Todas", "Combos", "Días especiales", "Vegetarianas")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filtros) { filtro ->
                    val activa = (categoriaFiltro == filtro)
                    Surface(
                        color = if (activa) PizzaRed else SurfaceCard,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .clickable { categoriaFiltro = filtro }
                    ) {
                        Text(
                            text = filtro,
                            color = if (activa) Color.White else TextPrimary,
                            fontWeight = if (activa) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp)
                        )
                    }
                }
            }
        }

        // TARJETAS DE PROMOS
        items(promosFiltradas, key = { it.id }) { promo ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Imagen grande con badges
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        AsyncImage(
                            model = promo.imageUrl,
                            contentDescription = promo.titulo,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Badges superiores
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = promo.badge1Color,
                                shape = RoundedCornerShape(50.dp)
                            ) {
                                Text(
                                    text = promo.badge1Texto,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            if (promo.badge2Texto != null && promo.badge2Color != null) {
                                Surface(
                                    color = promo.badge2Color,
                                    shape = RoundedCornerShape(50.dp)
                                ) {
                                    Text(
                                        text = promo.badge2Texto,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // Badge inferior en la imagen
                        Surface(
                            color = Color.White.copy(alpha = 0.92f),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = promo.badgeInferiorTexto,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Contenido inferior
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = promo.titulo,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = promo.descripcion,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = promo.vigencia,
                                fontSize = 11.sp,
                                color = TextMuted
                            )

                            Button(
                                onClick = {
                                    if (promo.id == "combofamiliar") {
                                        appState.agregarProductoAlCarrito(ComboFamiliar(), 1)
                                    } else {
                                        appState.agregarProductoAlCarrito(PizzaPromocional(), 1)
                                    }
                                    appState.mensajeNotificacion = "¡Promoción aplicada y agregada al carrito!"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PizzaRed),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalOffer,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Aplicar promo",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
