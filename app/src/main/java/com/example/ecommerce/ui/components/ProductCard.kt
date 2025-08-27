package com.example.ecommerce.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun ProductCard(
    product: Product,
    onAddToCart: (Product) -> Unit,
    onClick: (() -> Unit)? = null,
    quantity: Int = 0,
    onIncrement: (() -> Unit)? = null,
    onDecrement: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() },
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
            if (quantity <= 0) {
                Button(onClick = { onAddToCart(product) }) {
                    Text("Add to Cart")
                }
            } else {
                Row(Modifier.padding(top = 4.dp)) {
                    IconButton(onClick = { onDecrement?.invoke() }) {
                        Icon(Icons.Rounded.Remove, contentDescription = "decrease")
                    }
                    Text("$quantity", modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = { onIncrement?.invoke() }) {
                        Icon(Icons.Rounded.Add, contentDescription = "increase")
                    }
                }
            }
        }
    }
}


