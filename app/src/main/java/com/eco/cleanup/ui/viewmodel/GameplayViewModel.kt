package com.eco.cleanup.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eco.cleanup.data.local.entity.SampahEntity
import com.eco.cleanup.data.repository.GameRepository
import com.eco.cleanup.data.repository.SettingsRepository
import com.eco.cleanup.util.AudioManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val MAX_NYAWA  = 3
private const val POIN_BENAR = 10

data class GameplayUiState(
    val sampahList        : List<SampahEntity>    = emptyList(),
    val currentIndex      : Int                   = 0,
    val currentSampah     : SampahEntity?         = null,
    val nyawa             : Int                   = MAX_NYAWA,
    val skor              : Int                   = 0,
    val showSuccessModal  : Boolean               = false,  // Modal +10 di panel 3 Figma
    val showSalahModal    : Boolean               = false,  // Modal feedback salah
    val isDone            : Boolean               = false,
    val isWin             : Boolean               = false,
    val isPaused          : Boolean               = false,
    val isLoading         : Boolean               = true
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

    fun loadSampah() {
        viewModelScope.launch {
            val list = repo.getSampahForLevel(levelId)
            if (list.isNotEmpty()) {
                _state.value = GameplayUiState(
                    sampahList       = list,
                    currentIndex     = 0,
                    currentSampah    = list[0],
                    nyawa            = MAX_NYAWA,
                    skor             = 0,
                    showSuccessModal = false,
                    showSalahModal   = false,
                    isDone           = false,
                    isWin            = false,
                    isPaused         = false,
                    isLoading        = false
                )
            }
        }
    }

    /** Dipanggil saat item di-drop ke tong sampah */
    fun onItemDropped(idKategoriTong: Int) {
        val st = _state.value
        if (st.showSuccessModal || st.showSalahModal || st.isDone || st.isPaused) return

        val sampah = st.currentSampah ?: return

        viewModelScope.launch {
            val efekEnabled = settings.efekSuara.first()
            AudioManager.playLempar(context, efekEnabled)

            if (sampah.idKategori == idKategoriTong) {
                // ✅ Benar (+10)
                val newSkor = st.skor + POIN_BENAR
                _state.value = st.copy(
                    skor             = newSkor,
                    showSuccessModal = true
                )
            } else {
                // ❌ Salah (kurangi nyawa)
                val newNyawa = st.nyawa - 1
                if (newNyawa <= 0) {
                    AudioManager.playKalah(context, efekEnabled)
                    _state.value = st.copy(
                        nyawa          = 0,
                        showSalahModal = true,
                        isDone         = true,
                        isWin          = false
                    )
                } else {
                    _state.value = st.copy(
                        nyawa          = newNyawa,
                        showSalahModal = true
                    )
                }
            }
        }
    }

    /** Pemain menekan tombol SELANJUTNYA pada modal popup */
    fun onNextClicked() {
        val st = _state.value
        if (st.isDone && !st.isWin) {
            // Sudah game over (kalah)
            return
        }

        val nextIndex = st.currentIndex + 1
        if (nextIndex >= st.sampahList.size) {
            // Semua item level ini selesai (Menang!)
            viewModelScope.launch {
                val efekEnabled = settings.efekSuara.first()
                AudioManager.playMenang(context, efekEnabled)
                val bintang = hitungBintang(st.nyawa)
                repo.saveHasil(levelId, bintang, st.skor)
            }
            _state.value = st.copy(
                showSuccessModal = false,
                showSalahModal   = false,
                isDone           = true,
                isWin            = true
            )
        } else {
            // Lanjut ke item berikutnya
            _state.value = st.copy(
                currentIndex     = nextIndex,
                currentSampah    = st.sampahList[nextIndex],
                showSuccessModal = false,
                showSalahModal   = false
            )
        }
    }

    fun onPause()   { _state.value = _state.value.copy(isPaused = true) }
    fun onResume()  { _state.value = _state.value.copy(isPaused = false) }
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
