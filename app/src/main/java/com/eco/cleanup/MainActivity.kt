package com.eco.cleanup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.eco.cleanup.data.repository.SettingsRepository
import com.eco.cleanup.navigation.CleanUpNavGraph
import com.eco.cleanup.ui.theme.CleanUpTheme
import com.eco.cleanup.util.AudioManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsRepo = SettingsRepository(applicationContext)
        AudioManager.init(applicationContext, settingsRepo, lifecycleScope)

        enableEdgeToEdge()
        setContent {
            CleanUpTheme {
                CleanUpNavGraph()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        AudioManager.pauseBacksound()
    }

    override fun onStart() {
        super.onStart()
        AudioManager.resumeBacksound()
    }

    override fun onDestroy() {
        super.onDestroy()
        AudioManager.stopBacksound()
    }
}
