package com.example.jenstore.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = CutCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 10.dp, bottomEnd = 10.dp),
    extraLarge = RoundedCornerShape(80.dp),
    small = RoundedCornerShape(20.dp),
    medium = RoundedCornerShape(50.dp),
    large = RoundedCornerShape(70.dp),
)