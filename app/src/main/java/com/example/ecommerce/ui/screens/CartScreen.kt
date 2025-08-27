package com.example.ecommerce.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecommerce.viewmodel.CartViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onNavigateToHome: () -> Unit,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showEmptyCartDialog by remember { mutableStateOf(false) }
    var showOrderSuccessDialog by remember { mutableStateOf(false) }

    // checkout handle
    val handleCheckout = {
        if (cartViewModel.groupedCartItems.isEmpty()) {
            showEmptyCartDialog = true
        } else {
            showOrderSuccessDialog = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(cartViewModel.groupedCartItems) { cartItem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { onItemClick(cartItem.product.id) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Image
                        AsyncImage(
                            model = cartItem.product.image,
                            contentDescription = cartItem.product.title,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray.copy(alpha = 0.1f)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Product details
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = cartItem.product.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Black
                            )
                            Text(
                                text = cartItem.product.category,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                text = "$${cartItem.product.price}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4285F4),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Quantity managing
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = { cartViewModel.decrement(cartItem.product.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.Remove,
                                    contentDescription = "decrease",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        Color.Gray.copy(alpha = 0.1f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${cartViewModel.getQuantity(cartItem.product.id)}",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            }

                            IconButton(
                                onClick = { cartViewModel.increment(cartItem.product.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.Add,
                                    contentDescription = "increase",
                                    tint = Color(0xFF4285F4),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Delete Button
                        IconButton(
                            onClick = { cartViewModel.removeItem(cartItem.product.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Delete,
                                contentDescription = "delete",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Summary
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Items' count
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Items (${cartViewModel.groupedCartItems.sumOf { it.quantity }})",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$${cartViewModel.totalPrice}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Shipping
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Shipping",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Free",
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // total
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "$${cartViewModel.totalPrice}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4285F4)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Checkout button
                Button(
                    onClick = handleCheckout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4285F4)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Proceed to Checkout",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // empty order
    if (showEmptyCartDialog) {
        AlertDialog(
            onDismissRequest = { showEmptyCartDialog = false },
            title = {
                Text(
                    text = "Cart Empty",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            },
            text = {
                Text(
                    text = "Your cart is empty. Go and shop!",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showEmptyCartDialog = false
                        onNavigateToHome()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4285F4)
                    )
                ) {
                    Text(
                        text = "Go to Home",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEmptyCartDialog = false }
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.Gray
                    )
                }
            }
        )
    }

    // successful order
    if (showOrderSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showOrderSuccessDialog = false },
            title = {
                Text(
                    text = "Order Successful! 🎉",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF4CAF50)
                )
            },
            text = {
                Text(
                    text = "Your order is successful. Thank you for shopping!",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showOrderSuccessDialog = false
                        cartViewModel.clearAll()
                        onNavigateToHome()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text(
                        text = "Continue Shopping",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        )
    }
}