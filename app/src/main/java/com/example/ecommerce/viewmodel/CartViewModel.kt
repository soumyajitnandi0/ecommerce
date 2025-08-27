package com.example.ecommerce.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.model.CartItem

class CartViewModel : ViewModel() {

    // backwards compatible flat list (still exposed)
    var cartItems by mutableStateOf<List<Product>>(emptyList())
        private set

    // if same item present multiple times in the cart
    var groupedCartItems by mutableStateOf<List<CartItem>>(emptyList())
        private set

    private val idToQuantity = mutableStateMapOf<Int, Int>()
    private val idToProduct = mutableStateMapOf<Int, Product>()

    val totalPrice: Double
        get() = groupedCartItems.sumOf { it.product.price * it.quantity }

    fun addToCart(product: Product) {
        idToProduct[product.id] = product
        idToQuantity[product.id] = (idToQuantity[product.id] ?: 0) + 1
        recomputeState()
    }

    fun addToCart(product: Product, quantity: Int) {
        if (quantity <= 0) return
        idToProduct[product.id] = product
        idToQuantity[product.id] = (idToQuantity[product.id] ?: 0) + quantity
        recomputeState()
    }

    fun removeFromCart(product: Product) {
        val current = (idToQuantity[product.id] ?: 0) - 1
        if (current <= 0) {
            idToQuantity.remove(product.id)
            idToProduct.remove(product.id)
        } else {
            idToQuantity[product.id] = current
        }
        recomputeState()
    }

    fun increment(productId: Int) {
        val product = idToProduct[productId] ?: return
        addToCart(product)
    }

    fun decrement(productId: Int) {
        val product = idToProduct[productId] ?: return
        removeFromCart(product)
    }

    fun getQuantity(productId: Int): Int = idToQuantity[productId] ?: 0

    private fun recomputeState() {
        groupedCartItems = idToQuantity.mapNotNull { (id, qty) ->
            val product = idToProduct[id]
            product?.let { CartItem(it, qty) }
        }
        cartItems = groupedCartItems.flatMap { ci -> List(ci.quantity) { ci.product } }
    }
}


