package com.example.parvaznama.view.anim

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.parvaznama.R

@Composable
fun LoadingAirplaneAnim() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.plane))
    val progress by animateLottieCompositionAsState(composition, iterations = Int.MAX_VALUE)
   Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
       LottieAnimation(
           modifier = Modifier.size(300.dp).clip(CircleShape),
           composition = composition,
           progress = { progress },

           )
   }

}