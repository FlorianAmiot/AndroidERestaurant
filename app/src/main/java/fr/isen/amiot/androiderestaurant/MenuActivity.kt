package fr.isen.amiot.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.amiot.androiderestaurant.basket.Basket
import fr.isen.amiot.androiderestaurant.basket.BasketActivity
import fr.isen.amiot.androiderestaurant.network.Category
import fr.isen.amiot.androiderestaurant.network.Dish
import fr.isen.amiot.androiderestaurant.network.MenuResult
import fr.isen.amiot.androiderestaurant.network.NetworkConstants
import fr.isen.amiot.androiderestaurant.ui.theme.AndroidERestaurantTheme
import org.json.JSONObject

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = (intent.getSerializableExtra(CATEGROY_EXTRA_KEY) as? DishType) ?: DishType.STARTER

        setContent {
            AndroidERestaurantTheme {
                MenuView(type)
            }
        }
        Log.d("lifeCycle", "Menu Activity - OnCreate")
    }

    override fun onPause() {
        Log.d("lifeCycle", "Menu Activity - OnPause")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifeCycle", "Menu Activity - OnResume")
    }

    override fun onDestroy() {
        Log.d("lifeCycle", "Menu Activity - onDestroy")
        super.onDestroy()
    }

    companion object {
        val CATEGROY_EXTRA_KEY = "CATEGROY_EXTRA_KEY"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuView(type: DishType) {
    val category = remember {
        mutableStateOf<Category?>(null)
    }

    val context = LocalContext.current


    Column(Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = {
                Text(
                    text = "Menu",
                    modifier = Modifier.weight(300f),
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

        Text(
            text = "~ ${type.title()} ~",
            style = TextStyle(
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            category.value?.let {
                items(it.items) { it ->
                    dishRow(it)
                }
            }
        }
    }
    postData(type, category)
}

@Composable fun dishRow(dish: Dish) {
    val context = LocalContext.current
    Card(border =  BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.DISH_EXTRA_KEY, dish)
                intent.putExtra(DetailActivity.DISH_IMAGE_EXTRA_KEY, dish.images.first())
                intent.putExtra(
                    DetailActivity.DISH_INGREDIENTS_EXTRA_KEY,
                    dish.ingredients.joinToString(", ") { it.name })
                intent.putExtra(DetailActivity.DISH_PRICE_EXTRA_KEY, dish.prices.first().price)
                context.startActivity(intent)
            }
    ) {
        Row(Modifier.padding(8.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(dish.images.first())
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
            Text(dish.name,
                Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(8.dp)
                    .weight(5f)
            )
            Spacer(Modifier.weight(1f))
            Text("${dish.prices.first().price} €",
                Modifier.align(alignment = Alignment.CenterVertically))
        }
    }
}

@Composable
fun postData(type: DishType, category: MutableState<Category?>) {
    val currentCategory = type.title()
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)

    val params = JSONObject()
    params.put(NetworkConstants.ID_SHOP, "1")

    val request = JsonObjectRequest(
        Request.Method.POST,
        NetworkConstants.URL,
        params,
        { response ->
            Log.d("request", response.toString(2))
            val result = GsonBuilder().create().fromJson(response.toString(), MenuResult::class.java)
            val filteredResult = result.data.first { categroy -> categroy.name == currentCategory }
            category.value = filteredResult
        },
        {
            Log.e("request", it.toString())
        }
    )

    queue.add(request)

}