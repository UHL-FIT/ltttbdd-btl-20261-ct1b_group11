package com.btl.buddybudget.ui.vi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.repo.Repo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
class ViViewModel(
    val repo: Repo
) : ViewModel() {

    private val _uiState = MutableStateFlow(ViScreenState())

    val uiState: StateFlow<ViScreenState> = _uiState.asStateFlow()

    init {

        loadWallets()
    }

    private fun loadWallets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                combine(
                    repo.layViVaSoDu(),
                    repo.layTongTaiSan()
                ) { wallets, total ->
                    ViScreenState(
                        wallets = wallets,
                        tongTaiSan = total,
                        isLoading = false
                    )
                }.collect {
                    _uiState.value = it
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Lỗi tải danh sách ví: ${e.message}") }
            }
        }
    }
}