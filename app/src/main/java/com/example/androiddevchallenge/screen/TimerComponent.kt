package com.example.androiddevchallenge.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicatorComponent(progressValue: Float) {

    CircularProgressIndicator(
        progress = progressValue, strokeWidth = 7.dp, color = Color(0xFF9BC6DE) ,modifier = Modifier
            .background(color = Color.Transparent).padding( 2.dp)
            .fillMaxSize()
    )

}
