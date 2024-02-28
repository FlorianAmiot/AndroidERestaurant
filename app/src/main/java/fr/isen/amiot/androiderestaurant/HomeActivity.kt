package fr.isen.amiot.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.amiot.androiderestaurant.ui.theme.AndroidERestaurantTheme

enum class DishType {
    STARTER, MAIN, DESSERT;

    @Composable
    fun title(): String {
        return when(this) {
            STARTER -> stringResource(id = R.string.menu_starter)
            MAIN -> stringResource(id = R.string.menu_main)
            DESSERT -> stringResource(id = R.string.menu_dessert)
        }
    }
}

interface MenuInterface {
    fun dishPressed(dishType: DishType)
}

class HomeActivity : ComponentActivity(), MenuInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SetupView(this@HomeActivity)
                }
            }
        }
        Log.d("lifeCycle", "Home Activity - OnCreate")
    }

    override fun dishPressed(dishType: DishType) {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra(MenuActivity.CATEGROY_EXTRA_KEY, dishType)
        startActivity(intent)
    }

    override fun onPause() {
        Log.d("lifeCycle", "Home Activity - OnPause")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifeCycle", "Home Activity - OnResume")
    }

    override fun onDestroy() {
        Log.d("lifeCycle", "Home Activity - onDestroy")
        super.onDestroy()
    }
}

@Composable
fun SetupView(menu: MenuInterface) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.eiffel_tower_logo),
            contentDescription = "Logo Tour Eiffel",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .size(70.dp)
        )

        Text(
            text = "Bienvenue dans notre restaurant 'Le Petit Parisien, Cuisine traditionnelle'",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic, // Définir le style de police en italique
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.start_image),
            contentDescription = null,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
        CustomButton(type = DishType.STARTER, menu)
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
        CustomButton(type = DishType.MAIN, menu)
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
        CustomButton(type = DishType.DESSERT, menu)
    }
}



@Composable
fun CustomButton(type: DishType, menu: MenuInterface) {
    Button(
        onClick = { menu.dishPressed(type) },
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(type.title())
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidERestaurantTheme {
        SetupView(HomeActivity())
    }
}