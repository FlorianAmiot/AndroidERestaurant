package fr.isen.amiot.androiderestaurant.basket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fr.isen.amiot.androiderestaurant.R
import fr.isen.amiot.androiderestaurant.basket.ui.theme.AndroidERestaurantTheme
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

class BasketActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BasketView()
                }
            }
        }
    }
}



@Composable
fun BasketView() {
    val context = LocalContext.current
    val basketItems = remember {
        mutableStateListOf<BasketItem>()
    }

    basketItems.clear() // Effacez la liste existante pour éviter les duplications
    basketItems.addAll(Basket.current(context).items)

    val totalPrice = Basket.current(context).calculateTotalPrice(basketItems) // Obtenir le prix total

    Column(modifier = Modifier.fillMaxSize()) {
        if (basketItems.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espacement vertical entre chaque élément
            )  {
                items(basketItems) {
                    BasketItemView(it, basketItems)
                }
            }
            Button(
                onClick = {
                    // Simuler le passage de la commande
                    Toast.makeText(context, "Commande effectuée", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Commander ($totalPrice €)") // Afficher le prix total entre parenthèses
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Panier vide")
            }
        }
    }
}


@Composable
fun BasketItemView(item: BasketItem, basketItems: MutableList<BasketItem>) {
    Card {
        val context = LocalContext.current
        Card(border =  BorderStroke(1.dp, Color.Black),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(Modifier.padding(8.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.dish.images.first())
                        .build(),
                    null,
                    placeholder = painterResource(R.drawable.image_plat),
                    error = painterResource(R.drawable.image_plat),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(10))
                        .padding(8.dp)
                )
                Column(
                    Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .padding(horizontal = 8.dp)
                        .weight(15f)
                ) {
                    Text(
                        text = item.dish.name,
                        maxLines = 2, // Afficher sur deux lignes au maximum
                    )
                    Text("${item.dish.prices.first().price} €")
                }

                Spacer(Modifier.weight(1f))
                Text(item.count.toString(),
                    Modifier.align(alignment = Alignment.CenterVertically))
                Button(onClick = {
                    // Supprimer l'article et réafficher la vue
                    Basket.current(context).delete(item, context)
                    basketItems.clear()
                    basketItems.addAll(Basket.current(context).items)
                }) {
                    Text("X")
                }
            }
        }
    }
}


