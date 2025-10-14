package com.example.parvaznama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import cafe.adriel.voyager.navigator.Navigator
import com.example.parvaznama.view.navigation.SplashSc


import com.example.parvaznama.ui.theme.ParvaznamaTheme
import com.example.parvaznama.view.navigation.HomeSc
import com.orhanobut.hawk.Hawk
import java.lang.reflect.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb()),
        )
        Hawk.init(this).build()
        setContent {

            ParvaznamaTheme {
                Scaffold { innerPad ->
                    Box(androidx.compose.ui.Modifier.padding(innerPad).background(MaterialTheme.colorScheme.onBackground)) {
                        if(Hawk.get("splash",true)){
                            Navigator(SplashSc)

                        }else{
                            Navigator(HomeSc)

                        }

                    }
                }


            }
        }
    }
}


