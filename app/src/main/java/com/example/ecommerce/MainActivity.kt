@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ecommerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.ui.screens.CartScreen
import com.example.ecommerce.ui.screens.ProductDetailsScreen
import com.example.ecommerce.ui.screens.ProductListScreen
import com.example.ecommerce.ui.theme.EcommerceTheme
import com.example.ecommerce.viewmodel.CartViewModel
import com.example.ecommerce.viewmodel.ProductViewModel
import com.example.ecommerce.viewmodel.ProductViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcommerceTheme {
                val repository = remember { ProductRepository() }
                val productViewModel: ProductViewModel = viewModel(
                    factory = ProductViewModelFactory(repository)
                )
                val cartViewModel: CartViewModel = viewModel()

                var showCart by remember { mutableStateOf(false) }
                var selectedProduct by remember { mutableStateOf<Product?>(null) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(when {
                                selectedProduct != null -> "Product Details"
                                showCart -> "Cart"
                                else -> "Products"
                            }) },
                            navigationIcon = {},
                            actions = {
                                IconButton(onClick = {
                                    if (selectedProduct != null) {
                                        selectedProduct = null
                                    } else {
                                        showCart = !showCart
                                    }
                                }) {
                                    Text(
                                        when {
                                            selectedProduct != null -> "Back"
                                            showCart -> "Products"
                                            else -> "Cart"
                                        }
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    when {
                        selectedProduct != null -> ProductDetailsScreen(
                            product = selectedProduct!!,
                            cartViewModel = cartViewModel,
                            onBack = { selectedProduct = null },
                            modifier = Modifier.padding(innerPadding)
                        )
                        showCart -> CartScreen(cartViewModel, modifier = Modifier.padding(innerPadding))
                        else -> ProductListScreen(
                            productViewModel,
                            cartViewModel,
                            modifier = Modifier.padding(innerPadding),
                            onProductClick = { id ->
                                selectedProduct = productViewModel.productList.firstOrNull { it.id == id }
                            }
                        )
                    }
                }
            }
        }
    }
}