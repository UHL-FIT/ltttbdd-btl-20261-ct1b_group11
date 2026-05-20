package com.btl.buddybudget.ui.danhmuc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.repo.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DanhMucViewModel(private val repo: Repo) : ViewModel() {

    private val _uiState = MutableStateFlow(DanhMucScreenState())
    val uiState: StateFlow<DanhMucScreenState> = _uiState.asStateFlow()

    init {
        loadDanhMucs()
    }

    private fun loadDanhMucs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // Giả sử repo có hàm layTatCaDanhMuc() trả về Flow
            repo.layTatCaDanhMuc().collect { list ->
                _uiState.value = _uiState.value.copy(danhMucs = list, isLoading = false)
            }
        }
    }
/*
    fun themDanhMuc(danhMuc: DanhMuc) {
        viewModelScope.launch {
            repo.themDanhMuc(danhMuc)
        }
    }

    fun xoaDanhMuc(danhMuc: DanhMuc) {
        viewModelScope.launch {
            repo.xoaDanhMuc(danhMuc)
        }
    }
 */
}