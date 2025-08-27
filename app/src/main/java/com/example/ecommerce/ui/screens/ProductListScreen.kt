package com.example.ecommerce.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import com.example.ecommerce.ui.components.ProductCard
import com.example.ecommerce.viewmodel.CartViewModel
import com.example.ecommerce.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onProductClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val products = productViewModel.productList
    val (query, setQuery) = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (products.isEmpty()) {
            productViewModel.fetchProducts()
        }
    }

    val filtered = if (query.isBlank()) products else products.filter {
        it.title.contains(query, ignoreCase = true)
    }

    Column(modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = setQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            placeholder = { Text("Search products...") },
            singleLine = true
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(filtered) { product ->
                ProductCard(
                    product,
                    onAddToCart = { cartViewModel.addToCart(it) },
                    onClick = { onProductClick(product.id) },
                    quantity = cartViewModel.getQuantity(product.id),
                    onIncrement = { cartViewModel.increment(product.id) },
                    onDecrement = { cartViewModel.decrement(product.id) }
                )
            }
        }
    }
}


