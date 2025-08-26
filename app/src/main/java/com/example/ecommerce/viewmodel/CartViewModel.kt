package com.example.ecommerce.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ecommerce.data.model.Product

class CartViewModel : ViewModel() {

    var cartItems by mutableStateOf<List<Product>>(emptyList())
        private set

    val totalPrice: Double
        get() = cartItems.sumOf { it.price }

    fun addToCart(product: Product) {
        cartItems = cartItems + product
    }

    fun removeFromCart(product: Product) {
        cartItems = cartItems - product
    }
}


