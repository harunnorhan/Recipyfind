package com.example.therecipeapp.feature.greeting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.therecipeapp.R
import com.example.therecipeapp.ui.theme.backgroundDark
import com.example.therecipeapp.ui.theme.backgroundLight
import com.example.therecipeapp.ui.theme.primaryTextDark
import com.example.therecipeapp.ui.theme.primaryTextLight


@Composable
fun GreetingScreen(
    onStartClick: () -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (!isDarkTheme) backgroundLight else backgroundDark),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.greeting_background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(150.dp))
                Text(
                    text = "Search Recipes",
                    style = MaterialTheme.typography.displayMedium,
                    color = if (!isDarkTheme) primaryTextLight else primaryTextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Search for the recipes you want by your meal type.",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (!isDarkTheme) primaryTextLight else primaryTextDark
                )
                Spacer(modifier = Modifier.height(16.dp))
                ElevatedButton(
                    onClick = onStartClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isDarkTheme) primaryTextLight else primaryTextDark,
                        contentColor = if (!isDarkTheme) backgroundLight else backgroundDark
                    )
                ) {
                    Text(text = "Get Started !", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
