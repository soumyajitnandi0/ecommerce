package com.example.ecommerce.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ecommerce.ui.components.ProductCard
import com.example.ecommerce.viewmodel.CartViewModel
import com.example.ecommerce.viewmodel.ProductViewModel
import kotlinx.coroutines.delay

@Composable
fun ProductListScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onProductClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val products = productViewModel.productList
    var showFilters by remember { mutableStateOf(false) }
    val (query, setQuery) = remember { mutableStateOf("") }
    var debouncedQuery by remember { mutableStateOf("") }
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var minRating by remember { mutableStateOf(0) }
    var selectedPriceRange by remember { mutableStateOf<PriceRange?>(null) }
    var sortOption by remember { mutableStateOf(SortOption.None) }

    LaunchedEffect(Unit) {
        if (products.isEmpty()) {
            productViewModel.fetchProducts()
        }
    }

    LaunchedEffect(query) {
        delay(300)
        debouncedQuery = query
    }

    val categories = remember(products) { products.map { it.category }.distinct().sorted() }

    var filtered = products
    if (debouncedQuery.isNotBlank()) {
        filtered = filtered.filter {
            it.title.contains(debouncedQuery, ignoreCase = true) ||
                    it.category.contains(debouncedQuery, ignoreCase = true)
        }
    }
    if (selectedCategories.isNotEmpty()) {
        filtered = filtered.filter { selectedCategories.contains(it.category) }
    }
    selectedPriceRange?.let { range ->
        filtered = filtered.filter { p ->
            val price = p.price
            val minOk = range.min == null || price >= range.min
            val maxOk = range.max == null || price < range.max
            minOk && maxOk
        }
    }
    if (minRating > 0) {
        filtered = filtered.filter { (it.rating.rate).toInt() >= minRating }
    }
    filtered = when (sortOption) {
        SortOption.PriceAsc -> filtered.sortedBy { it.price }
        SortOption.PriceDesc -> filtered.sortedByDescending { it.price }
        SortOption.RatingDesc -> filtered.sortedByDescending { it.rating.rate }
        SortOption.None -> filtered
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = setQuery,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "Search products...",
                        color = Color(0xFF9CA3AF)
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedTextColor = Color(0xFF1F2937),
                    unfocusedTextColor = Color(0xFF1F2937)
                ),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3B82F6))
                            .clickable { showFilters = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.Tune,
                            contentDescription = "Filters",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        }

        // Product Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(filtered) { product ->
                ProductCard(
                    product = product,
                    onAddToCart = { cartViewModel.addToCart(it) },
                    onClick = { onProductClick(product.id) }
                )
            }
        }
    }

    if (showFilters) {
        Dialog(
            onDismissRequest = { showFilters = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filters",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )

                        Row {
                            TextButton(
                                onClick = {
                                    selectedCategories = emptySet()
                                    minRating = 0
                                    selectedPriceRange = null
                                    sortOption = SortOption.None
                                }
                            ) {
                                Text(
                                    "Reset",
                                    color = Color(0xFF3B82F6)
                                )
                            }

                            IconButton(
                                onClick = { showFilters = false }
                            ) {
                                Icon(
                                    Icons.Rounded.Close,
                                    contentDescription = "Close",
                                    tint = Color(0xFF6B7280)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Category Section
                        FilterSection(title = "Category") {
                            CategoryChips(
                                categories = categories,
                                selectedCategories = selectedCategories,
                                onCategoryToggle = { cat ->
                                    selectedCategories = if (selectedCategories.contains(cat)) {
                                        selectedCategories - cat
                                    } else {
                                        selectedCategories + cat
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Price Range Section
                        FilterSection(title = "Price Range") {
                            PriceRangeChips(
                                selectedPriceRange = selectedPriceRange,
                                onPriceRangeSelect = { range ->
                                    selectedPriceRange = if (selectedPriceRange == range) null else range
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Minimum Rating Section
                        FilterSection(title = "Minimum Rating") {
                            RatingChips(
                                minRating = minRating,
                                onRatingSelect = { rating ->
                                    minRating = if (minRating == rating) 0 else rating
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Sort By Section
                        FilterSection(title = "Sort By") {
                            SortChips(
                                sortOption = sortOption,
                                onSortSelect = { option ->
                                    sortOption = if (sortOption == option) SortOption.None else option
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Apply Button
                    Button(
                        onClick = { showFilters = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Apply Filters",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryChips(
    categories: List<String>,
    selectedCategories: Set<String>,
    onCategoryToggle: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            FilterChip(
                selected = selectedCategories.contains(category),
                onClick = { onCategoryToggle(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF3B82F6),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFFF3F4F6),
                    labelColor = Color(0xFF6B7280)
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PriceRangeChips(
    selectedPriceRange: PriceRange?,
    onPriceRangeSelect: (PriceRange) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        priceRanges.forEach { range ->
            FilterChip(
                selected = selectedPriceRange == range,
                onClick = { onPriceRangeSelect(range) },
                label = { Text(range.label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF3B82F6),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFFF3F4F6),
                    labelColor = Color(0xFF6B7280)
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RatingChips(
    minRating: Int,
    onRatingSelect: (Int) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val ratings = listOf(
            4 to "4+ Stars",
            3 to "3+ Stars",
            2 to "2+ Stars",
            1 to "1+ Stars"
        )

        ratings.forEach { (value, label) ->
            FilterChip(
                selected = minRating == value,
                onClick = { onRatingSelect(value) },
                label = { Text(label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF3B82F6),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFFF3F4F6),
                    labelColor = Color(0xFF6B7280)
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SortChips(
    sortOption: SortOption,
    onSortSelect: (SortOption) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val sortOptions = listOf(
            SortOption.PriceAsc to "Price: Low To High",
            SortOption.PriceDesc to "Price: High To Low",
            SortOption.RatingDesc to "Highest Rated"
        )

        sortOptions.forEach { (option, label) ->
            FilterChip(
                selected = sortOption == option,
                onClick = { onSortSelect(option) },
                label = { Text(label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF3B82F6),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFFF3F4F6),
                    labelColor = Color(0xFF6B7280)
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

private enum class SortOption { None, PriceAsc, PriceDesc, RatingDesc }

private data class PriceRange(val label: String, val min: Double?, val max: Double?)

private val priceRanges = listOf(
    PriceRange("Under $25", null, 25.0),
    PriceRange("$25 - $50", 25.0, 50.0),
    PriceRange("$50 - $100", 50.0, 100.0),
    PriceRange("$100 - $200", 100.0, 200.0),
    PriceRange("Over $200", 200.0, null)
)