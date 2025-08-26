package com.example.ecommerce.data.remote

import com.example.ecommerce.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("sort") sort: String? = null
    ): List<Product>
}


