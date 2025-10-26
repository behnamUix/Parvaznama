package com.example.parvaznama.view.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.parvaznama.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(title: String = "پروازنما", icon: Boolean = false) {
    TopAppBar(
        modifier = Modifier
            .height(85.dp)
            .fillMaxWidth(),
        title = {
            Text(
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            if (icon) {

                Icon(
                    modifier = Modifier.padding(16.dp),

                    tint = MaterialTheme.colorScheme.background,

                    painter = painterResource(R.drawable.icon_menu),
                    contentDescription = ""
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.onPrimary),

        navigationIcon = {
            if (icon) {
                Icon(
                    modifier = Modifier.padding(16.dp),

                    tint = MaterialTheme.colorScheme.background,
                    painter = painterResource(R.drawable.icon_notif),
                    contentDescription = ""
                )
            }
        }
    )
}


