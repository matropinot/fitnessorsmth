package com.homefit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.homefit.app.data.HomeFitRepository
import com.homefit.app.ui.HomeFitViewModel
import com.homefit.app.ui.HomeFitViewModelFactory
import com.homefit.app.ui.HomeFitApp
import com.homefit.app.ui.theme.HomeFitTheme

class MainActivity : ComponentActivity() {
    private val viewModel: HomeFitViewModel by viewModels {
        HomeFitViewModelFactory(HomeFitRepository(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeFitTheme {
                HomeFitApp(viewModel)
            }
        }
    }
}
