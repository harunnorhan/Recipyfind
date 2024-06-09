package com.example.therecipeapp.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberUpdatedState
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
import com.example.therecipeapp.feature.error.ErrorView
import com.example.therecipeapp.feature.loading.LoadingView
import com.example.therecipeapp.models.recipes.RecipeModel
import com.example.therecipeapp.ui.theme.backgroundDark
import com.example.therecipeapp.ui.theme.backgroundLight
import com.example.therecipeapp.ui.theme.homeBackgroundDark
import com.example.therecipeapp.ui.theme.primaryTextDark
import com.example.therecipeapp.ui.theme.primaryTextLight
import com.example.therecipeapp.ui.theme.topIconColor
import kotlinx.coroutines.launch

val mealTypes = listOf(
    "Main Course",
    "Side Dish",
    "Dessert",
    "Appetizer",
    "Salad",
    "Bread",
    "Breakfast",
    "Soup",
    "Beverage",
    "Sauce",
    "Marinade",
    "Fingerfood",
    "Snack",
    "Drink"
)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onRecipeClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit,
    imageLoader: ImageLoader,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchRecipes(state.selectedMealType)
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
        ){
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            onFavoritesClick()
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .padding(8.dp)
                            .background(color = if (!isDarkTheme) backgroundLight else primaryTextDark, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite Button",
                            tint = topIconColor
                        )
                    }
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Hi there!",
                        style = MaterialTheme.typography.displayMedium,
                        color = if (!isDarkTheme) primaryTextLight else primaryTextDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "What do you want to find?",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (!isDarkTheme) primaryTextLight else primaryTextDark
                    )
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                mealTypes.forEach { mealType ->
                    Button(
                        onClick = { viewModel.fetchRecipes(mealType) },
                        modifier = Modifier.padding(horizontal = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors =
                            if (state.selectedMealType == mealType) {
                                ButtonDefaults.buttonColors(
                                    containerColor = if (!isDarkTheme) backgroundLight else backgroundDark,
                                    contentColor = if (!isDarkTheme) primaryTextLight else primaryTextDark,
                                )
                            } else {
                                ButtonDefaults.buttonColors(
                                    containerColor = if (!isDarkTheme) primaryTextLight else primaryTextDark,
                                    contentColor = if (!isDarkTheme) backgroundLight else backgroundDark
                                )
                            }
                    ) {
                        Text(text = mealType, style = MaterialTheme.typography.titleMedium)
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
                        RecipeCards(
                            viewModel = viewModel,
                            recipes = state.recipes,
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
fun RecipeCards(
    viewModel: HomeViewModel = hiltViewModel(),
    recipes: List<RecipeModel>,
    onRecipeClick: (Int) -> Unit,
    imageLoader: ImageLoader,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(recipes) { recipe ->
            val isFavorite by rememberUpdatedState(viewModel.isRecipeFavorite(recipe.id))

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
                            text = recipe.title ?: "No title",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (!isDarkTheme) Color.White else homeBackgroundDark
                        )
                    }

                    IconButton(
                        onClick = {
                            viewModel.toggleFavorite(recipeId = recipe.id)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(32.dp)
                            .offset(x = (-8).dp, y = (8).dp)
                            .background(Color.White, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = "Favorite Button",
                            tint = Color.Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



