package com.example.ecommerce.data.repository

import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.remote.RetrofitInstance

class ProductRepository {
    suspend fun getProducts(limit: Int, sort: String? = null): List<Product> {
        return RetrofitInstance.api.getProducts(limit, sort)
    }
}


