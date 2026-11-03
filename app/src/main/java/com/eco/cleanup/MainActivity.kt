package com.eco.cleanup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.eco.cleanup.navigation.CleanUpNavGraph
import com.eco.cleanup.ui.theme.CleanUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CleanUpTheme {
                CleanUpNavGraph()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        com.eco.cleanup.util.AudioManager.pauseBacksound()
    }

    override fun onStart() {
        super.onStart()
        com.eco.cleanup.util.AudioManager.resumeBacksound(enabled = true)
    }

    override fun onDestroy() {
        super.onDestroy()
        com.eco.cleanup.util.AudioManager.stopBacksound()
    }
}
