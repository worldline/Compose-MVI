package com.worldline.composemvi.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.worldline.composemvi.presentation.ui.components.NiaBackground
import com.worldline.composemvi.presentation.ui.components.NiaGradientBackground
import com.worldline.composemvi.presentation.ui.foryou.ForYouScreen
import com.worldline.composemvi.presentation.ui.theme.ComposeMVITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMVITheme {
                NiaBackground {
                    NiaGradientBackground {
                        Scaffold(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                        ) {
                            ForYouScreen(
                                modifier = Modifier
                                    .padding(it)
                            )
                        }
                    }
                }
            }
        }
    }
}
