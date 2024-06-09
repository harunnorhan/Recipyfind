package com.example.therecipeapp.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.therecipeapp.R
import com.example.therecipeapp.data.source.local.entity.LocalRecipes
import com.example.therecipeapp.feature.error.ErrorView
import com.example.therecipeapp.feature.loading.LoadingView
import com.example.therecipeapp.ui.theme.backgroundLight
import com.example.therecipeapp.ui.theme.homeBackgroundDark
import com.example.therecipeapp.ui.theme.primaryTextDark
import com.example.therecipeapp.ui.theme.primaryTextLight
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onRecipeClick: (Int) -> Unit,
    imageLoader: ImageLoader,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchFavoriteRecipes()
    }

    LaunchedEffect(viewModel.message) {
        viewModel.message.collect { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

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
                Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(
                        onClick = { onBack() },
                        modifier = Modifier
                            .size(40.dp)
                            .padding(8.dp)
                            .background(
                                color = if (!isDarkTheme) backgroundLight else primaryTextDark,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon",
                            tint = primaryTextLight
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Favorite Recipes",
                        style = MaterialTheme.typography.displayMedium,
                        color = if (!isDarkTheme) primaryTextLight else primaryTextDark
                    )

                    if (state.recipes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Here are your favorite recipes!",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (!isDarkTheme) primaryTextLight else primaryTextDark
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                when {
                    state.isLoading -> LoadingView()
                    state.isError -> ErrorView()
                    else -> {
                        FavoriteRecipeCards(
                            recipes = state.recipes,
                            viewModel = viewModel,
                            onRecipeClick = onRecipeClick,
                            imageLoader = imageLoader
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteRecipeCards(
    viewModel: FavoritesViewModel = hiltViewModel(),
    recipes: List<LocalRecipes>,
    onRecipeClick: (Int) -> Unit,
    imageLoader: ImageLoader,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    if (recipes.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(recipes) { recipe ->

                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                        .clickable { onRecipeClick(recipe.id) }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(recipe.image)
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
                                .fillMaxWidth()
                                .background(
                                    color = if (!isDarkTheme) primaryTextLight else primaryTextDark,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = recipe.title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (!isDarkTheme) Color.White else homeBackgroundDark
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.deleteRecipe(recipe.id)
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(32.dp)
                                .offset(x = (-8).dp, y = (8).dp)
                                .background(Color.White, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Button",
                                tint = primaryTextLight
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "There are no recipes here yet...",
                style = MaterialTheme.typography.titleMedium,
                color = if (!isDarkTheme) primaryTextLight else primaryTextDark
            )
        }
    }
}