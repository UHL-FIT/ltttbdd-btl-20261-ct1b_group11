package com.btl.buddybudget.ui.vi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.btl.buddybudget.data.repo.Repo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class ViViewModel( // ◄ THÊM @Inject constructor
    private val repo: Repo
) : ViewModel() {

    private val _uiState = MutableStateFlow(ViScreenState())

    val uiState: StateFlow<ViScreenState> = _uiState.asStateFlow()

    init {

        loadWallets()
    }

    private fun loadWallets() {

        viewModelScope.launch {

            combine(

                repo.layViVaSoDu(),

                repo.layTongTaiSan()

            ) { wallets, total ->

                ViScreenState(

                    wallets = wallets,

                    tongTaiSan = total

                )
            }.collect {

                _uiState.value = it
            }
        }
    }
}