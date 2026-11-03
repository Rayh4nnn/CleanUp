package com.eco.cleanup.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eco.cleanup.data.local.entity.SampahEntity
import com.eco.cleanup.data.repository.GameRepository
import com.eco.cleanup.data.repository.SettingsRepository
import com.eco.cleanup.util.AudioManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Berapa item per level
private const val ITEM_PER_LEVEL = 5
private const val MAX_NYAWA      = 3
private const val POIN_BENAR     = 10

data class GameplayUiState(
    val sampahList      : List<SampahEntity>    = emptyList(),
    val currentIndex    : Int                   = 0,
    val currentSampah   : SampahEntity?         = null,
    val nyawa           : Int                   = MAX_NYAWA,
    val skor            : Int                   = 0,
    val showInfoPopup   : Boolean               = true,   // tampilkan edukasi sebelum drag
    val showSkorPopup   : Boolean               = false,  // +10 popup
    val showSalahPopup  : Boolean               = false,  // info setelah salah
    val isDone          : Boolean               = false,
    val isWin           : Boolean               = false,
    val isPaused        : Boolean               = false,
    val isLoading       : Boolean               = true
)

class GameplayViewModel(
    private val repo        : GameRepository,
    private val settings    : SettingsRepository,
    private val context     : Context,
    private val levelId     : Int
) : ViewModel() {

    private val _state = MutableStateFlow(GameplayUiState())
    val state: StateFlow<GameplayUiState> = _state

    init {
        loadSampah()
    }

    private fun loadSampah() {
        viewModelScope.launch {
            val list = repo.getSampahForLevel(levelId)
            if (list.isNotEmpty()) {
                _state.value = _state.value.copy(
                    sampahList   = list,
                    currentIndex = 0,
                    currentSampah = list[0],
                    showInfoPopup = true,
                    isLoading    = false
                )
            }
        }
    }

    /** Pemain menekan SELANJUTNYA setelah membaca popup edukasi */
    fun onInfoDismissed() {
        _state.value = _state.value.copy(showInfoPopup = false, showSalahPopup = false)
    }

    /** Dipanggil saat item di-drop ke tong (idKategoriTong = id tong yang dituju) */
    fun onItemDropped(idKategoriTong: Int) {
        val st = _state.value
        if (st.showInfoPopup || st.showSkorPopup || st.showSalahPopup || st.isDone) return

        val sampah = st.currentSampah ?: return
        val efekOn = true // akan dibaca dari settings di launch

        viewModelScope.launch {
            val efekEnabled = settings.efekSuara.first()

            if (sampah.idKategori == idKategoriTong) {
                // ✅ Benar
                AudioManager.playLempar(context, efekEnabled)
                val newSkor = st.skor + POIN_BENAR
                _state.value = st.copy(skor = newSkor, showSkorPopup = true)
                delay(1200)
                _state.value = _state.value.copy(showSkorPopup = false)
                goToNextItem(benar = true)
            } else {
                // ❌ Salah
                AudioManager.playLempar(context, efekEnabled)
                val newNyawa = st.nyawa - 1
                if (newNyawa <= 0) {
                    // Game over
                    AudioManager.playKalah(context, efekEnabled)
                    _state.value = st.copy(
                        nyawa        = 0,
                        showSalahPopup = true,
                        isDone       = true,
                        isWin        = false
                    )
                } else {
                    _state.value = st.copy(nyawa = newNyawa, showSalahPopup = true)
                }
            }
        }
    }

    /** Setelah popup salah ditutup, lanjut ke item berikutnya */
    private fun goToNextItem(benar: Boolean) {
        val st = _state.value
        val nextIndex = st.currentIndex + 1
        if (nextIndex >= st.sampahList.size) {
            // Semua item habis → menang
            viewModelScope.launch {
                val efekEnabled = settings.efekSuara.first()
                AudioManager.playMenang(context, efekEnabled)
            }
            val bintang = hitungBintang(st.nyawa)
            viewModelScope.launch {
                repo.saveHasil(levelId, bintang, st.skor)
            }
            _state.value = st.copy(isDone = true, isWin = true)
        } else {
            _state.value = st.copy(
                currentIndex  = nextIndex,
                currentSampah = st.sampahList[nextIndex],
                showInfoPopup = true
            )
        }
    }

    fun onPause()  { _state.value = _state.value.copy(isPaused = true) }
    fun onResume() { _state.value = _state.value.copy(isPaused = false) }
    fun onRestart() { loadSampah() }

    fun getBintang(): Int = hitungBintang(_state.value.nyawa)

    private fun hitungBintang(nyawa: Int): Int = when {
        nyawa >= 3 -> 3
        nyawa == 2 -> 2
        nyawa == 1 -> 1
        else       -> 0
    }
}

class GameplayViewModelFactory(
    private val repo     : GameRepository,
    private val settings : SettingsRepository,
    private val context  : Context,
    private val levelId  : Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        GameplayViewModel(repo, settings, context, levelId) as T
}

