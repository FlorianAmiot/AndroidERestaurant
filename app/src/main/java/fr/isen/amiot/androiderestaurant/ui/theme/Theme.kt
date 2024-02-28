package fr.isen.amiot.androiderestaurant.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import fr.isen.amiot.androiderestaurant.basket.ui.theme.Blue40
import fr.isen.amiot.androiderestaurant.basket.ui.theme.BlueLight40
import fr.isen.amiot.androiderestaurant.basket.ui.theme.BlueLight80
import fr.isen.amiot.androiderestaurant.basket.ui.theme.BlueSea40
import fr.isen.amiot.androiderestaurant.basket.ui.theme.BlueSea80


private val DarkColorScheme = darkColorScheme(
    primary = BlueSea80, // Couleur primaire bleu foncé
    secondary = BlueSea80, // Couleur secondaire bleu mer foncé
    tertiary = BlueLight80 // Couleur tertiaire bleu clair
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40, // Couleur primaire bleu clair
    secondary = BlueSea40, // Couleur secondaire bleu mer clair
    tertiary = BlueLight40 // Couleur tertiaire bleu clair
)


        /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

@Composable
fun AndroidERestaurantTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        // Dynamic color is available on Android 12+
        dynamicColor: Boolean = true,
        content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
    )
}