package com.example.ecommerce.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RatingBar(
    rating: Double,
    count: Int?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 16.dp,
    starColor: Color = Color(0xFFFFC107),
    showText: Boolean = true
) {
    val fullStars = kotlin.math.floor(rating).toInt().coerceIn(0, 5)
    val hasHalf = (rating - fullStars) >= 0.5 && fullStars < 5
    val emptyStars = 5 - fullStars - if (hasHalf) 1 else 0

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Stars
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = starColor,
                modifier = Modifier.size(iconSize)
            )
        }
        if (hasHalf) {
            Icon(
                imageVector = Icons.Rounded.StarHalf,
                contentDescription = null,
                tint = starColor,
                modifier = Modifier.size(iconSize)
            )
        }
        repeat(emptyStars) {
            Icon(
                imageVector = Icons.Rounded.StarOutline,
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.3f),
                modifier = Modifier.size(iconSize)
            )
        }

        // Rating text and count
        if (showText) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = String.format("%.1f", rating),
                style = MaterialTheme.typography.labelMedium,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
            count?.let {
                Text(
                    text = " ($it)",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}