package com.example.therecipeapp.feature.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.therecipeapp.ui.theme.homeBackgroundDark

@Composable
fun LoadingView(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (!isDarkTheme) Color.White else homeBackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = if (!isDarkTheme) homeBackgroundDark else Color.White
        )
    }
}