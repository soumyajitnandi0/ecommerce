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
    var isLoadingMore by mutableStateOf(false)
    private var currentPage = 0
    private val pageSize = 20
    private var endReached = false

    fun fetchProducts(limit: Int = pageSize, sort: String? = null) {
        viewModelScope.launch {
            isLoading = true
            try {
                currentPage = 0
                endReached = false
                val items = repository.getProducts(limit, sort)
                productList = items
                if (items.size < limit) endReached = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isLoading = false
        }
    }

    fun loadMore(sort: String? = null) {
        if (isLoading || isLoadingMore || endReached) return
        viewModelScope.launch {
            isLoadingMore = true
            try {
                val nextLimit = (currentPage + 2) * pageSize
                val items = repository.getProducts(nextLimit, sort)
                if (items.size <= productList.size) {
                    endReached = true
                } else {
                    productList = items
                    currentPage += 1
                    if (items.size < nextLimit) endReached = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isLoadingMore = false
        }
    }
}


