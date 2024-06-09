package com.example.therecipeapp.feature.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.therecipeapp.R
import com.example.therecipeapp.feature.error.ErrorView
import com.example.therecipeapp.feature.loading.LoadingView
import com.example.therecipeapp.models.informations.InformationModel
import com.example.therecipeapp.ui.theme.homeBackgroundDark
import com.example.therecipeapp.ui.theme.primaryTextDark
import com.example.therecipeapp.ui.theme.primaryTextLight
import com.example.therecipeapp.utils.parseHtmlToAnnotatedString
import kotlinx.coroutines.launch

@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    id: Int,
    onBack: () -> Unit,
    imageLoader: ImageLoader
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(id) {
        viewModel.loadRecipeData(recipeId = id)
    }

    LaunchedEffect(viewModel.message) {
        viewModel.message.collect { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    val isFavorite by remember(state.isFavorite) {
        derivedStateOf { state.isFavorite }
    }

    when {
        state.isLoading -> LoadingView()
        state.isError -> ErrorView()
        else -> state.information?.let { information ->
            RecipeDetailView(
                information = information,
                onBack = onBack,
                imageLoader = imageLoader,
                snackbarHostState = snackbarHostState,
                isFavorite = isFavorite,
                onFavoriteToggle = {
                    viewModel.toggleFavorite()
                }
            )
        }
    }
}

@Composable
fun RecipeDetailView(
    information: InformationModel,
    onBack: () -> Unit,
    imageLoader: ImageLoader,
    snackbarHostState: SnackbarHostState,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    var isIngredientExpanded by remember { mutableStateOf(false) }
    var isInstructionsExpanded by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(if (!isDarkTheme) Color.White else homeBackgroundDark)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3 / 2f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(information.image)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = "recipe image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    imageLoader = imageLoader
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    IconButton(
                        onClick = { onBack() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon",
                            tint = primaryTextLight
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isFavorite) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = "Favorite Icon",
                            tint = Color.Red
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                            .background(if (!isDarkTheme) Color.White else homeBackgroundDark)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = information.title ?: "no title",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge,
                                color = if (!isDarkTheme) primaryTextLight else Color.White
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        Modifier
                            .wrapContentSize()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.users_sharp_solid),
                            contentDescription = "Users Icon",
                            modifier = Modifier.size(24.dp),
                            tint = if (!isDarkTheme) primaryTextLight else Color.White
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Servings: ${information.servings}",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (!isDarkTheme) primaryTextLight else Color.White
                        )
                    }


                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        Modifier
                            .wrapContentSize()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.clock_sharp_solid),
                            contentDescription = "Clock Icon",
                            modifier = Modifier.size(24.dp),
                            tint = if (!isDarkTheme) primaryTextLight else Color.White
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Ready In Minutes: ${information.readyInMinutes}",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (!isDarkTheme) primaryTextLight else Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ingredients",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            color = if (!isDarkTheme) primaryTextLight else Color.White
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { isIngredientExpanded = !isIngredientExpanded },
                        ) {
                            if (!isIngredientExpanded) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "Arrow Down Icon",
                                    tint = if (!isDarkTheme) primaryTextLight else primaryTextDark
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowUp,
                                    contentDescription = "Arrow Up Icon",
                                    tint = if (!isDarkTheme) primaryTextLight else primaryTextDark
                                )
                            }
                        }
                    }

                    when (isIngredientExpanded) {
                        true -> {
                            information.ingredients?.forEach { ingredient ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "•",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (!isDarkTheme) primaryTextLight else Color.White
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        text = "${ingredient?.original}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (!isDarkTheme) primaryTextLight else Color.White
                                    )
                                }
                            }
                        }
                        else -> {
                            //ignored
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Instructions",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            color = if (!isDarkTheme) primaryTextLight else Color.White
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { isInstructionsExpanded = !isInstructionsExpanded },
                        ) {
                            if (!isInstructionsExpanded) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "Arrow Down Icon",
                                    tint = if (!isDarkTheme) primaryTextLight else Color.White
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowUp,
                                    contentDescription = "Arrow Up Icon",
                                    tint = if (!isDarkTheme) primaryTextLight else Color.White
                                )
                            }
                        }
                    }

                    when (isInstructionsExpanded) {
                        true -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = parseHtmlToAnnotatedString("${information.instructions}"),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (!isDarkTheme) primaryTextLight else Color.White
                                )
                            }
                        }

                        else -> {
                            //ignored
                        }
                    }
                }
            }
        }
    }
}