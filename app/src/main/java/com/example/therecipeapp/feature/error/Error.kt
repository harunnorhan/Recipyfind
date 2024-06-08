package com.example.therecipeapp.feature.error

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.therecipeapp.ui.theme.homeBackgroundDark

@Composable
fun ErrorView(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (!isDarkTheme) Color.White else homeBackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "An error occurred. Please try again.",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = if (!isDarkTheme) homeBackgroundDark else Color.White
        )
    }
}