package com.example.parvaznama.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// 🎨 مقادیر فاصله‌ها (بر اساس Material Design 8dp grid system)
data class Spacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp
)

// CompositionLocal برای دسترسی در MaterialTheme
val LocalSpacing = staticCompositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    get() = LocalSpacing.current