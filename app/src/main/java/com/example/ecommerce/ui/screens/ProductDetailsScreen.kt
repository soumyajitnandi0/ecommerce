package com.example.ecommerce.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.viewmodel.CartViewModel

@Composable
fun ProductDetailsScreen(
    product: Product,
    cartViewModel: CartViewModel,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val (qty, setQty) = remember { mutableStateOf(1) }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        AsyncImage(
            model = product.image,
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.height(16.dp))
        Text(product.category.uppercase())
        Text(product.title, fontWeight = FontWeight.SemiBold)
        Text("₹${product.price}", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("Description")
        Text(product.description)
        Spacer(Modifier.height(16.dp))
        Text("Quantity", fontWeight = FontWeight.SemiBold)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            IconButton(onClick = { if (qty > 1) setQty(qty - 1) }) {
                Icon(Icons.Rounded.Remove, contentDescription = "decrease")
            }
            Text("$qty", modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { setQty(qty + 1) }) {
                Icon(Icons.Rounded.Add, contentDescription = "increase")
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = { repeat(qty) { cartViewModel.addToCart(product) } }, modifier = Modifier.fillMaxWidth()) {
            Text("Add to Cart")
        }
    }
}


