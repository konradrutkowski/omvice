package com.krutkowski.omvice.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColors(
    primary = Color.White,
    primaryVariant = Blue500,
    onPrimary = Color.Black,
    secondary = Blue500,
    secondaryVariant = Blue200,
    onSecondary = Color.White,
    error = Red800,
    onBackground = Color.Black,
)

@Composable
fun OmviceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightThemeColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}