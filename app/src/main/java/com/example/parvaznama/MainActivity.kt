package com.example.parvaznama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.example.parvaznama.view.navigation.SplashSc


import com.example.parvaznama.ui.theme.ParvaznamaTheme
import com.example.parvaznama.view.component.ToolbarComp
import com.example.parvaznama.view.navigation.HomeSc
import com.orhanobut.hawk.Hawk

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.White.toArgb(), darkScrim = Color.Transparent.toArgb()

            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.White.toArgb(), darkScrim = Color.Transparent.toArgb()

            )
        )
        Hawk.init(this).build()
        setContent {

            ParvaznamaTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(

                            actions = {
                                Icon(
                                    tint = MaterialTheme.colorScheme.background,
                                    modifier = Modifier.padding(16.dp),
                                    painter = painterResource(R.drawable.icon_menu),
                                    contentDescription = ""
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                            title = {
                                Text(
                                    color = MaterialTheme.colorScheme.background,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    text="پرواز نما",
                                    style = MaterialTheme.typography.headlineSmall) },
                            navigationIcon = {
                                Icon(
                                    modifier = Modifier.padding(16.dp),
                                    tint = MaterialTheme.colorScheme.background,
                                    painter = painterResource(R.drawable.icon_notif),
                                    contentDescription = ""
                                )
                            }
                        )
                    }
                ) { innerPad ->

                    Box(
                        androidx.compose.ui.Modifier
                            .padding(innerPad)
                            .background(MaterialTheme.colorScheme.onBackground)
                    ) {
                        if (Hawk.get("splash", true)) {
                            Navigator(SplashSc)

                        } else {
                            Navigator(HomeSc)

                        }

                    }

                }


            }
        }
    }
}


