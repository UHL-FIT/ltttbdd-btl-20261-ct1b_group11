package com.btl.buddybudget.ui.danhmuc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.repo.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DanhMucViewModel(private val repo: Repo) : ViewModel() {

    private val _uiState = MutableStateFlow(DanhMucScreenState())
    val uiState: StateFlow<DanhMucScreenState> = _uiState.asStateFlow()

    init {
        loadDanhMucs()
    }


    private fun loadDanhMucs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repo.layTatCaDanhMuc().collect { list ->
                _uiState.update { it.copy(danhMucs = list, isLoading = false) }
            }
        }
    }

}