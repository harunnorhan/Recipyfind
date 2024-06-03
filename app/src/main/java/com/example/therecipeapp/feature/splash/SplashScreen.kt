package com.example.therecipeapp.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.therecipeapp.ui.theme.Purple40
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: (Boolean) -> Unit,
) {

    LaunchedEffect(Unit) {
        delay(2000)
        onSplashFinished(true)
    }

    Scaffold(
        containerColor = Purple40,
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(
                text = "Splash Screen",
                style = TextStyle(
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}