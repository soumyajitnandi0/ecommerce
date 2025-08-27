package com.example.ecommerce.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.model.CartItem
import com.example.ecommerce.data.local.CartDao
import com.example.ecommerce.data.local.CartItemEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartViewModel(private val cartDao: CartDao? = null) : ViewModel() {

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

    fun removeItem(productId: Int) {
        idToQuantity.remove(productId)
        idToProduct.remove(productId)
        recomputeState()
    }

    fun clearAll() {
        idToQuantity.clear()
        idToProduct.clear()
        recomputeState()
    }

    private fun recomputeState() {
        groupedCartItems = idToQuantity.mapNotNull { (id, qty) ->
            val product = idToProduct[id]
            product?.let { CartItem(it, qty) }
        }
        cartItems = groupedCartItems.flatMap { ci -> List(ci.quantity) { ci.product } }

        // Persist to Room if available
        cartDao?.let { dao ->
            viewModelScope.launch {
                val entities = groupedCartItems.map { ci ->
                    CartItemEntity(
                        productId = ci.product.id,
                        title = ci.product.title,
                        price = ci.product.price,
                        image = ci.product.image,
                        category = ci.product.category,
                        description = ci.product.description,
                        rating = ci.product.rating.rate,
                        ratingCount = ci.product.rating.count,
                        quantity = ci.quantity
                    )
                }
                dao.clear()
                dao.upsertAll(entities)
            }
        }
    }

    init {
        // Hydrate from Room if available
        cartDao?.let { dao ->
            viewModelScope.launch {
                dao.observeCart().collectLatest { entities ->
                    idToQuantity.clear()
                    idToProduct.clear()
                    entities.forEach { e ->
                        idToQuantity[e.productId] = e.quantity
                        idToProduct[e.productId] = Product(
                            id = e.productId,
                            title = e.title,
                            price = e.price,
                            description = e.description,
                            category = e.category,
                            image = e.image,
                            rating = com.example.ecommerce.data.model.Rating(e.rating, e.ratingCount)
                        )
                    }
                    // Recompute snapshots without writing back to DB
                    groupedCartItems = idToQuantity.mapNotNull { (id, qty) ->
                        val product = idToProduct[id]
                        product?.let { CartItem(it, qty) }
                    }
                    cartItems = groupedCartItems.flatMap { ci -> List(ci.quantity) { ci.product } }
                }
            }
        }
    }

    // duplicate removed
}