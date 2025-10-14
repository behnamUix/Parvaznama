package com.example.parvaznama.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

// 🎨 فقط Dark ColorScheme
private val LarkColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    error = Error,
    background = Background,
    surface = Surface,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onBackground = OnBackground,
    onSurface = OnSurface,
    onError = OnError,

)

@Composable
fun ParvaznamaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LarkColorScheme,
        typography = AppTypography
    ) {
        CompositionLocalProvider(
            LocalSpacing provides Spacing()
        ) {
            content()
        }
    }
}