package com.example.ecommerce.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecommerce.viewmodel.CartViewModel

@Composable
fun CartScreen(cartViewModel: CartViewModel) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn {
            items(cartViewModel.cartItems) { product ->
                Text("${product.title} - ₹${product.price}")
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Total: ₹${cartViewModel.totalPrice}", fontWeight = FontWeight.Bold)
    }
}


