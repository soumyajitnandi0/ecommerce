package com.example.ecommerce.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    var productList by mutableStateOf<List<Product>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)

    fun fetchProducts(limit: Int = 10, sort: String? = null) {
        viewModelScope.launch {
            isLoading = true
            try {
                productList = repository.getProducts(limit, sort)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isLoading = false
        }
    }
}


