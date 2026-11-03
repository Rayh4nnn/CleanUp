package com.eco.cleanup.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eco.cleanup.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HasilUiState(
    val levelId      : Int     = 1,
    val isWin        : Boolean = false,
    val skor         : Int     = 0,
    val bintang      : Int     = 0,
    val nextLevelId  : Int?    = null,
    val tips         : String  = ""
)

class HasilViewModel(
    private val repo    : GameRepository,
    private val levelId : Int,
    private val isWin   : Boolean,
    private val skor    : Int,
    private val bintang : Int
) : ViewModel() {

    private val _state = MutableStateFlow(HasilUiState())
    val state: StateFlow<HasilUiState> = _state

    init {
        viewModelScope.launch {
            val nextLevel = repo.getLevelById(levelId + 1)
            _state.value = HasilUiState(
                levelId     = levelId,
                isWin       = isWin,
                skor        = skor,
                bintang     = bintang,
                nextLevelId = if (isWin && nextLevel != null) nextLevel.idLevel else null,
                tips        = getTips(levelId)
            )
        }
    }

    private fun getTips(levelId: Int): String = when (levelId) {
        1 -> "Sampah organik bisa dijadikan kompos untuk menyuburkan tanaman di rumah!"
        2 -> "Botol plastik butuh 400 tahun untuk terurai. Yuk, kurangi pemakaian plastik!"
        else -> "Setiap aksi kecil dalam memilah sampah membuat bumi kita lebih sehat!"
    }
}

class HasilViewModelFactory(
    private val repo    : GameRepository,
    private val levelId : Int,
    private val isWin   : Boolean,
    private val skor    : Int,
    private val bintang : Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        HasilViewModel(repo, levelId, isWin, skor, bintang) as T
}

