package com.eco.cleanup.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eco.cleanup.data.local.entity.LevelEntity
import com.eco.cleanup.data.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class PetaLevelViewModel(private val repo: GameRepository) : ViewModel() {

    val levels: StateFlow<List<LevelEntity>> = repo.getAllLevel()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class PetaLevelViewModelFactory(private val repo: GameRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PetaLevelViewModel(repo) as T
}

