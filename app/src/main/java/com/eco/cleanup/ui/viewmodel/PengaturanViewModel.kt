package com.eco.cleanup.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eco.cleanup.data.repository.SettingsRepository
import com.eco.cleanup.util.AudioManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PengaturanViewModel(
    private val settings : SettingsRepository,
    private val context  : Context
) : ViewModel() {

    val musikLatar: StateFlow<Boolean> = settings.musikLatar
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val efekSuara: StateFlow<Boolean> = settings.efekSuara
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setMusikLatar(enabled: Boolean) {
        viewModelScope.launch {
            settings.setMusikLatar(enabled)
            AudioManager.setBacksoundEnabled(enabled)
        }
    }

    fun setEfekSuara(enabled: Boolean) {
        viewModelScope.launch { settings.setEfekSuara(enabled) }
    }
}

class PengaturanViewModelFactory(
    private val settings : SettingsRepository,
    private val context  : Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PengaturanViewModel(settings, context) as T
}

