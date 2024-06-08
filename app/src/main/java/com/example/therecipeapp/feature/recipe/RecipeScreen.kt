package com.example.therecipeapp.feature.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.example.therecipeapp.feature.error.ErrorView
import com.example.therecipeapp.feature.loading.LoadingView
import com.example.therecipeapp.models.informations.InformationModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    id: Int,
    onBack: () -> Unit,
    imageLoader: ImageLoader,
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(id) {
        viewModel.loadRecipeData(recipeId = id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Recipe")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.toggleFavorite()
                    }) {
                        if (state.isFavorited) {
                            Text(text = "delete from Favorites")

                        } else {
                            Text(text = "Add to Favorites")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                when {
                    state.isLoading -> LoadingView()
                    state.isError -> ErrorView()
                    else -> state.information?.let { information ->
                        RecipeDetailView(
                            state.recipeId,
                            information
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeDetailView(
    recipeId: Int,
    information: InformationModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = information.title.toString(), style = MaterialTheme.typography.titleSmall)
        Text(text = information.image.toString(), style = MaterialTheme.typography.titleSmall)
        Text(text = recipeId.toString(), style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))

        information.instructions?.let {
            Text(text = "Instructions:")
            Text(text = it)
        }

        information.ingredients?.let {
            Text(text = "Ingredients:")
            it.forEach { ingredient ->
                Text(text = ingredient?.original ?: "not found")
            }
        }
    }
}