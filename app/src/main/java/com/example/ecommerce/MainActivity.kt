@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ecommerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
import com.example.ecommerce.data.local.AppDatabase
import com.example.ecommerce.viewmodel.CartViewModelFactory

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
                val db = remember { AppDatabase.getInstance(applicationContext) }
                val cartViewModel: CartViewModel = viewModel(
                    factory = CartViewModelFactory(db.cartDao())
                )

                var showCart by remember { mutableStateOf(false) }
                var selectedProduct by remember { mutableStateOf<Product?>(null) }

                BackHandler(enabled = selectedProduct != null || showCart) {
                    when {
                        selectedProduct != null -> selectedProduct = null
                        showCart -> showCart = false
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(when {
                                selectedProduct != null -> "Product Details"
                                showCart -> "Cart"
                                else -> "Products"
                            }) },
                            navigationIcon = {
                                when {
                                    selectedProduct != null -> {
                                        IconButton(onClick = { selectedProduct = null }) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Back"
                                            )
                                        }
                                    }
                                    showCart -> {
                                        IconButton(onClick = { showCart = false }) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Back to Products"
                                            )
                                        }
                                    }
                                }
                            },
                            actions = {
                                when {
                                    showCart && cartViewModel.groupedCartItems.isNotEmpty() -> {
                                        // Clear All button for cart screen
                                        TextButton(
                                            onClick = { cartViewModel.clearAll() }
                                        ) {
                                            Text(
                                                text = "Clear All",
                                                color = Color.Red,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                    !showCart && selectedProduct == null -> {
                                        // Cart icon for product list screen
                                        IconButton(onClick = { showCart = true }) {
                                            Icon(
                                                Icons.Filled.ShoppingCart,
                                                contentDescription = "Cart"
                                            )
                                        }
                                    }
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
                            onNavigateToHome = { showCart = false; selectedProduct = null },
                            modifier = Modifier.padding(innerPadding)
                        )
                        showCart -> CartScreen(
                            cartViewModel = cartViewModel,
                            onNavigateToHome = { showCart = false; selectedProduct = null },
                            onItemClick = { id ->
                                selectedProduct = productViewModel.productList.firstOrNull { it.id == id }
                                if (selectedProduct != null) showCart = false
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
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