package fr.isen.amiot.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import fr.isen.amiot.androiderestaurant.basket.Basket
import fr.isen.amiot.androiderestaurant.basket.BasketActivity
import fr.isen.amiot.androiderestaurant.network.Dish
import fr.isen.amiot.androiderestaurant.ui.theme.AndroidERestaurantTheme
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest


class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra(DISH_EXTRA_KEY) as? Dish
        val dishImage = intent.getStringExtra(DISH_IMAGE_EXTRA_KEY)
        val dishIngredients = intent.getStringExtra(DISH_INGREDIENTS_EXTRA_KEY)
        val dishPrice = intent.getStringExtra(DISH_PRICE_EXTRA_KEY)


        setContent {
            AndroidERestaurantTheme {

                DishDetailView(dish, dishImage, dishIngredients, dishPrice)
            }
        }


    }

    companion object {
        const val DISH_EXTRA_KEY = "DISH_EXTRA_KEY"
        const val DISH_IMAGE_EXTRA_KEY = "DISH_IMAGE_EXTRA_KEY"
        const val DISH_INGREDIENTS_EXTRA_KEY = "DISH_INGREDIENTS_EXTRA_KEY"
        const val DISH_PRICE_EXTRA_KEY = "DISH_PRICE_EXTRA_KEY"
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDetailView(
    dish: Dish?,
    dishImage: String?,
    dishIngredients: String?,
    dishPrice: String?,

) {
    val context = LocalContext.current


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Affichage de la toolbar
        TopAppBar(
                title = {
                    Text(
                        text = "Détails du plat",
                        textAlign = TextAlign.Center
                    )
            },
            actions = {
                Box(contentAlignment = Alignment.Center) {
                }

                val numberOfItemsInCart = Basket.current(context).items.sumBy { it.count
                }
                    IconButton(onClick = { val intent = Intent(context, BasketActivity::class.java)
                        context.startActivity(intent)}) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Panier")
                    }
                    // Pastille pour afficher le nombre d'articles dans le panier
                    if (numberOfItemsInCart > 0) {
                        Surface(
                            color = Color.Red,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .size(16.dp)
                                .offset(-18.dp, (-13).dp) // Décalage pour le placer dans le coin supérieur droit
                        ) {
                            Text(
                                text = numberOfItemsInCart.toString(),
                                color = Color.White,
                                fontSize =  8.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(2.dp)
                            )
                        }
                    }

    }
    )

        dish?.let {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(dish.images.first())
                    .build(),
                null,
                placeholder = painterResource(R.drawable.image_plat),
                error = painterResource(R.drawable.image_plat)
            )


            Text(
                text = dish.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Ingrédients: $dishIngredients",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Prix: $dishPrice €",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
            QuantitySelector(dish)
        }
    }
}


@Composable
fun QuantitySelector(dish: Dish?) {
    var quantity by remember { mutableStateOf(1) }
    //dish price string to float
    val priceInt = dish?.prices?.first()?.price?.toFloat() ?: 0f
    var totalPrice = priceInt * quantity
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quantité: $quantity",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row {
            Button(
                onClick = { if (quantity > 1) quantity-- },
                modifier = Modifier.padding(end = 8.dp) // Ajouter de l'espace à droite du bouton "-"
            ) {
                Text(text = "-")
            }
            Button(
                onClick = { quantity++ },
                modifier = Modifier.padding(start = 8.dp) // Ajouter de l'espace à gauche du bouton "+"
            ) {
                Text(text = "+")
            }
        }
        Row(
        ) {
            Button(
                onClick = {
                    if (dish != null) {
                        Basket.current(context).add(dish, quantity, context)
                        Toast.makeText(context, "Plat ajouté au panier", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Commander")
            }
        }
    }

    Text(
        text = "Total: $totalPrice €",
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(8.dp)
    )
}