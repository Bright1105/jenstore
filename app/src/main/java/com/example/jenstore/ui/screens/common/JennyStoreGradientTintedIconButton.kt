package com.example.jenstore.ui.screens.common


import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jenstore.R
import com.example.jenstore.ui.theme.JenstoreTheme


//val color: List<Color> = listOf(
//    Color.DarkGray,
//    Color.Blue,
//    Color.Magenta
//)
////
//@Composable
//fun JennyStoreGradientTintedIconButton(
//    imageVector: ImageVector,
//    onClick: () -> Unit,
//    contentDescription: String?,
//    modifier: Modifier = Modifier,
//    colors: List<Color> = color
//) {
//    val interactionSource = remember { MutableInteractionSource() }
//
//    // This should use a layer + srcIn but needs investigation
//    val border = Modifier.fadeInDiagonalGradientBorder(
//        showBorder = true,
//        colors = color,
//        shape = CircleShape
//    )
//    val pressed by interactionSource.collectIsPressedAsState()
//    val background: Modifier = if (pressed) {
//        Modifier.offsetGradientBackground(colors, 200.dp, 0f)
//    } else {
//        Modifier.background(color)
//    }
//}