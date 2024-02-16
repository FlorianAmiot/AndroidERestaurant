package fr.isen.amiot.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import fr.isen.amiot.androiderestaurant.network.Dish
import fr.isen.amiot.androiderestaurant.ui.theme.AndroidERestaurantTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra(DISH_EXTRA_KEY) as? Dish
        val dishImage = intent.getStringExtra(DISH_IMAGE_EXTRA_KEY)
        val dishIngredients = intent.getStringExtra(DISH_INGREDIENTS_EXTRA_KEY)
        val dishPrice = intent.getStringExtra(DISH_PRICE_EXTRA_KEY)


        setContent {
            DishDetailView(dish, dishImage, dishIngredients, dishPrice)
        }
    }

    companion object {
        const val DISH_EXTRA_KEY = "DISH_EXTRA_KEY"
        const val DISH_IMAGE_EXTRA_KEY = "DISH_IMAGE_EXTRA_KEY"
        const val DISH_INGREDIENTS_EXTRA_KEY = "DISH_INGREDIENTS_EXTRA_KEY"
        const val DISH_PRICE_EXTRA_KEY = "DISH_PRICE_EXTRA_KEY"
    }
}

@Composable
fun DishDetailView(dish: Dish?, dishImage: String?, dishIngredients: String?, dishPrice: String?) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        dish?.let {
            if (dishImage != null) {
                Image(
                    painter = rememberImagePainter(dishImage),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }
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
        Button(onClick = { if (quantity > 1) quantity-- }) {
            Text(text = "-")
        }
        Button(onClick = { quantity++ }) {
            Text(text = "+")
        }
    }
    Text(
        text = "Total: $totalPrice €",
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(8.dp)
    )
}