package com.btl.buddybudget.ui.vi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuaViScreen(
    walletId: Int,
    onBack: () -> Unit,
    viewModel: SuaViViewModel
) {
    LaunchedEffect(walletId) {
        viewModel.loadWallet(walletId)
    }

    val state = viewModel.uiState
    var hienThiXacNhanXoa by remember { mutableStateOf(false) }

    // Hộp thoại cảnh báo khi nhấn nút Xóa
    if (hienThiXacNhanXoa) {
        AlertDialog(
            onDismissRequest = { hienThiXacNhanXoa = false },
            title = { Text(text = "Xóa ví", color = Color.White) },
            text = { Text(text = "Bạn có chắc chắn muốn xóa ví '${state.name}' không? Hành động này không thể hoàn tác nếu ví chứa giao dịch.", color = Color.LightGray) },
            containerColor = Color(0xFF1E1E1E),
            confirmButton = {
                TextButton(
                    onClick = {
                        hienThiXacNhanXoa = false
                        viewModel.xoaVi(onSuccess = onBack)
                    }
                ) {
                    Text("XÓA", color = Color(0xFFF44336))
                }
            },
            dismissButton = {
                TextButton(onClick = { hienThiXacNhanXoa = false }) {
                    Text("HỦY", color = Color.Gray)
                }
            }
        )
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text(text = "Sửa ví", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.capNhatVi(onSuccess = onBack) }
                    ) {
                        Text(text = "LƯU", color = Color(0xFF4CAF50), fontSize = 16.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Phần Form nhập liệu tương đương WalletForm
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = viewModel::doiTenVi,
                        label = { Text("Tên ví") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.soDu,
                        onValueChange = viewModel::doiSoDu,
                        label = { Text("Số dư ban đầu") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.donVi,
                        onValueChange = viewModel::doiDonVi,
                        label = { Text("Đơn vị tiền tệ") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card Trạng thái Lưu trữ (Ẩn ví)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lưu trữ ví (Tạm ẩn)",
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = state.isArchived,
                        onCheckedChange = viewModel::anVi
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // NÚT XÓA VÍ (Màu đỏ đặc trưng cho hành động phá hủy dữ liệu)
            Button(
                onClick = { hienThiXacNhanXoa = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F) // Màu đỏ đậm phù hợp Dark Theme
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Xóa ví",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "XÓA VÍ",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            // Hiển thị thông báo lỗi hệ thống/Validation (nếu có)
            state.error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}