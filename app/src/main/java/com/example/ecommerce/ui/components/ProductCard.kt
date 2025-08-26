package com.example.ecommerce.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ecommerce.data.model.Product

@Composable
fun ProductCard(product: Product, onAddToCart: (Product) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation()
    ) {
        Column(Modifier.padding(8.dp)) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(product.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("₹${product.price}", fontWeight = FontWeight.Bold)
            Button(onClick = { onAddToCart(product) }) {
                Text("Add to Cart")
            }
        }
    }
}


