package com.example.therecipeapp.feature.splash


import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.therecipeapp.R
import com.example.therecipeapp.ui.theme.primaryTextDark
import com.example.therecipeapp.ui.theme.primaryTextLight
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: (Boolean) -> Unit,
) {
    val scale = remember {
        Animatable(1f)
    }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.3f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(3000L)
        onSplashFinished(true)
    }

    Surface(
        color = primaryTextLight,
        modifier = Modifier.fillMaxSize()
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize().scale(scale.value),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "The Recipe App",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall,
                    color = primaryTextDark
                )
            }
        }
    }
}