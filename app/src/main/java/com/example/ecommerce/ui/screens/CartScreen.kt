package com.example.ecommerce.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecommerce.viewmodel.CartViewModel

@Composable
fun CartScreen(cartViewModel: CartViewModel, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(Modifier.weight(1f)) {
            items(cartViewModel.groupedCartItems) { cartItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(cartItem.product.title, maxLines = 1)
                        Text("₹${cartItem.product.price}")
                    }
                    Row {
                        IconButton(onClick = { cartViewModel.decrement(cartItem.product.id) }) {
                            Icon(Icons.Rounded.Remove, contentDescription = "decrease")
                        }
                        Text("${cartViewModel.getQuantity(cartItem.product.id)}", modifier = Modifier.padding(horizontal = 8.dp))
                        IconButton(onClick = { cartViewModel.increment(cartItem.product.id) }) {
                            Icon(Icons.Rounded.Add, contentDescription = "increase")
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total: ₹${cartViewModel.totalPrice}", fontWeight = FontWeight.Bold)
            Text("Items: ${cartViewModel.groupedCartItems.sumOf { it.quantity }}")
        }
    }
}


