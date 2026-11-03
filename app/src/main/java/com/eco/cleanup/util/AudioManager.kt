package com.eco.cleanup.util

import android.content.Context
import android.media.MediaPlayer

/**
 * Mengelola backsound (loop) dan efek suara (one-shot).
 * Dipanggil dari Composable via SideEffect / DisposableEffect.
 */
object AudioManager {

    private var backsoundPlayer: MediaPlayer? = null

    // ── Backsound ─────────────────────────────────────────────────

    fun startBacksound(context: Context, enabled: Boolean) {
        if (!enabled) return
        if (backsoundPlayer?.isPlaying == true) return
        backsoundPlayer = createPlayer(context, "suara/backsound.mpeg")?.apply {
            isLooping = true
            start()
        }
    }

    fun pauseBacksound() {
        backsoundPlayer?.takeIf { it.isPlaying }?.pause()
    }

    fun resumeBacksound(enabled: Boolean) {
        if (!enabled) return
        backsoundPlayer?.takeIf { !it.isPlaying }?.start()
    }

    fun stopBacksound() {
        backsoundPlayer?.stop()
        backsoundPlayer?.release()
        backsoundPlayer = null
    }

    fun setBacksoundEnabled(enabled: Boolean) {
        if (enabled) {
            backsoundPlayer?.takeIf { !it.isPlaying }?.start()
        } else {
            backsoundPlayer?.takeIf { it.isPlaying }?.pause()
        }
    }

    // ── Efek Suara ────────────────────────────────────────────────

    fun playSfx(context: Context, fileName: String, enabled: Boolean) {
        if (!enabled) return
        createPlayer(context, "suara/$fileName")?.apply {
            setOnCompletionListener { release() }
            start()
        }
    }

    fun playLempar(context: Context, enabled: Boolean)  = playSfx(context, "lempar.mpeg",  enabled)
    fun playMenang(context: Context, enabled: Boolean)  = playSfx(context, "menang.mpeg",  enabled)
    fun playKalah(context: Context, enabled: Boolean)   = playSfx(context, "kalah.mpeg",   enabled)

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

