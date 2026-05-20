package com.btl.buddybudget.ui.giaodich

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ThemGiaoDichViewModel : ViewModel() {

    // Quản lý trạng thái UI dưới dạng StateFlow
    private val _uiState = MutableStateFlow(ThemGiaoDichScreenState())
    val uiState: StateFlow<ThemGiaoDichScreenState> = _uiState.asStateFlow()

    // Thay đổi loại giao dịch (Thu / Chi)
    fun onTransactionTypeChanged(isExpense: Boolean) {
        _uiState.update { it.copy(idExpense = isExpense) }
    }

    // Thay đổi số tiền nhập vào
    fun onAmountChanged(newAmount: String) {
        // Chỉ cho phép nhập số
        if (newAmount.all { it.isDigit() } || newAmount.isEmpty()) {
            _uiState.update { it.copy(amount = newAmount) }
        }
    }

    // Cập nhật ví được chọn (Sau này kết nối DB chọn từ danh sách ví)
    fun onWalletSelected(walletName: String) {
        _uiState.update { it.copy(selectedWalletName = walletName) }
    }

    // Cập nhật nhóm được chọn (Khi từ trang Chọn nhóm trả kết quả về)
    fun onGroupSelected(groupName: String) {
        _uiState.update { it.copy(selectedGroupName = groupName) }
    }

    // Thực hiện lưu giao dịch vào database
    fun saveTransaction(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        if (currentState.amount.isEmpty() || currentState.amount == "0") {
            // Có thể xử lý báo lỗi nếu số tiền bằng 0 ở đây
            return
        }

        // TODO: Gọi Repository hoặc Database để Insert dữ liệu tại đây

        // Lưu thành công thì gọi callback để quay lại màn hình trước
        onSuccess()
    }
}