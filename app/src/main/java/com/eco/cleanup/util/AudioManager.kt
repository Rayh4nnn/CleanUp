package com.eco.cleanup.util

import android.content.Context
import android.media.MediaPlayer
import com.eco.cleanup.data.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Mengelola backsound (loop) dan efek suara (one-shot).
 * Terintegrasi secara terpusat dengan preferensi pengguna via SettingsRepository.
 */
object AudioManager {

    private var backsoundPlayer: MediaPlayer? = null
    private var appContext: Context? = null

    var isMusicEnabled: Boolean = true
        private set

    var isSfxEnabled: Boolean = true
        private set

    private var shouldPlayMusic: Boolean = false
    private var isInitialized: Boolean = false

    /**
     * Inisialisasi AudioManager dengan mengamati SettingsRepository.
     * Dipanggil sekali saat MainActivity dibuat.
     */
    fun init(context: Context, settingsRepo: SettingsRepository, scope: CoroutineScope) {
        appContext = context.applicationContext
        if (isInitialized) return
        isInitialized = true

        // Amati status musik latar dari DataStore
        settingsRepo.musikLatar
            .onEach { enabled ->
                isMusicEnabled = enabled
                if (!enabled) {
                    pauseBacksound()
                } else if (shouldPlayMusic) {
                    resumeBacksound()
                }
            }
            .launchIn(scope)

        // Amati status efek suara dari DataStore
        settingsRepo.efekSuara
            .onEach { enabled ->
                isSfxEnabled = enabled
            }
            .launchIn(scope)
    }

    // ── Backsound ─────────────────────────────────────────────────

    fun startBacksound(context: Context, enabled: Boolean = isMusicEnabled) {
        appContext = context.applicationContext
        shouldPlayMusic = true

        if (!isMusicEnabled || !enabled) return

        if (backsoundPlayer != null) {
            try {
                if (backsoundPlayer?.isPlaying == false) {
                    backsoundPlayer?.start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }

        backsoundPlayer = createPlayer(context, "suara/backsound.mpeg")?.apply {
            isLooping = true
            start()
        }
    }

    fun pauseBacksound() {
        try {
            backsoundPlayer?.takeIf { it.isPlaying }?.pause()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun resumeBacksound(enabled: Boolean = isMusicEnabled) {
        if (!isMusicEnabled || !enabled || !shouldPlayMusic) return

        if (backsoundPlayer == null) {
            appContext?.let { ctx ->
                startBacksound(ctx)
            }
            return
        }

        try {
            backsoundPlayer?.takeIf { !it.isPlaying }?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopBacksound() {
        shouldPlayMusic = false
        try {
            backsoundPlayer?.stop()
            backsoundPlayer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            backsoundPlayer = null
        }
    }

    fun setBacksoundEnabled(enabled: Boolean) {
        isMusicEnabled = enabled
        if (enabled) {
            if (shouldPlayMusic) {
                resumeBacksound()
            }
        } else {
            pauseBacksound()
        }
    }

    fun setSfxEnabled(enabled: Boolean) {
        isSfxEnabled = enabled
    }

    // ── Efek Suara ────────────────────────────────────────────────

    fun playSfx(context: Context, fileName: String, enabled: Boolean = isSfxEnabled) {
        if (!isSfxEnabled || !enabled) return
        try {
            createPlayer(context, "suara/$fileName")?.apply {
                setOnCompletionListener { mp ->
                    try {
                        mp.release()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playLempar(context: Context, enabled: Boolean = isSfxEnabled)  = playSfx(context, "lempar.mpeg",  enabled)
    fun playMenang(context: Context, enabled: Boolean = isSfxEnabled)  = playSfx(context, "menang.mpeg",  enabled)
    fun playKalah(context: Context, enabled: Boolean = isSfxEnabled)   = playSfx(context, "kalah.mpeg",   enabled)

    // ── Helper ────────────────────────────────────────────────────

    private fun createPlayer(context: Context, assetPath: String): MediaPlayer? {
        return try {
            val afd = context.assets.openFd(assetPath)
            MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepare()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
