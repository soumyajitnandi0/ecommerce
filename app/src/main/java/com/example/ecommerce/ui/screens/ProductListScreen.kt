package com.example.ecommerce.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.ecommerce.ui.components.ProductCard
import com.example.ecommerce.viewmodel.CartViewModel
import com.example.ecommerce.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(productViewModel: ProductViewModel, cartViewModel: CartViewModel) {
    val products = productViewModel.productList

    LaunchedEffect(Unit) {
        if (products.isEmpty()) {
            productViewModel.fetchProducts()
        }
    }

    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
        items(products) { product ->
            ProductCard(product, onAddToCart = { cartViewModel.addToCart(it) })
        }
    }
}


