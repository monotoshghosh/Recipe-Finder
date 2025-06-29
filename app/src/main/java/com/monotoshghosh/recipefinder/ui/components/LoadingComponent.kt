package com.monotoshghosh.recipefinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.monotoshghosh.recipefinder.R

@Composable
fun LoadingComponent() {
    Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.loading)
                .decoderFactory(GifDecoder.Factory())
                .crossfade(true)
                .build(),
            contentDescription = "Loading GIF",
            modifier = Modifier.size(200.dp)
        )
    }
}