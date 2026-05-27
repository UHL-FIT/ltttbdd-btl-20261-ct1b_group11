package com.btl.buddybudget.ui.vi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.btl.buddybudget.ui.danhmuc.DanhSachIcons
import com.btl.buddybudget.ui.danhmuc.DanhSachMau
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

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

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // --- NÚT QUAY VỀ HÌNH TRÒN ---
        Box(
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF1C1C1E))
                .clickable { onBack() }
                .align(Alignment.TopStart),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "‹",
                color = Color(0xFF0A84FF),
                fontSize = 28.sp,
                modifier = Modifier.offset(y = (-2).dp)
            )
        }

        // --- NÚT LƯU ---
        Text(
            text = "Lưu",
            color = Color(0xFF0A84FF),
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(end = 20.dp, top = 28.dp)
                .clickable { viewModel.capNhatVi(onSuccess = onBack) }
                .align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiêu đề căn giữa
            Text(
                text = "Sửa ví",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 28.dp, bottom = 20.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
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
                                unfocusedTextColor = Color.White,
                                focusedLabelColor = Color.Gray,
                                unfocusedLabelColor = Color.Gray
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
                                unfocusedTextColor = Color.White,
                                focusedLabelColor = Color.Gray,
                                unfocusedLabelColor = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        var expandedDonVi by remember { mutableStateOf(false) }
                        val listDonVi = listOf("VND", "USD", "EUR", "JPY")

                        ExposedDropdownMenuBox(
                            expanded = expandedDonVi,
                            onExpandedChange = { expandedDonVi = !expandedDonVi },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = state.donVi,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Đơn vị tiền tệ") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDonVi) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedLabelColor = Color.Gray,
                                    unfocusedLabelColor = Color.Gray,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                ),
                                modifier = Modifier
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedDonVi,
                                onDismissRequest = { expandedDonVi = false },
                                containerColor = Color(0xFF2C2C2E)
                            ) {
                                listDonVi.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption, color = Color.White) },
                                        onClick = {
                                            viewModel.doiDonVi(selectionOption)
                                            expandedDonVi = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // --- CHỌN ICON ---
                        Text("Chọn biểu tượng", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(DanhSachIcons) { icon ->
                                Box(
                                    modifier = Modifier
                                        .size(45.dp)
                                        .clip(CircleShape)
                                        .background(if (state.iconName == icon) Color.White.copy(0.2f) else Color(0xFF2C2C2E))
                                    .border(if (state.iconName == icon) 2.dp else 0.dp, Color.White, CircleShape)
                                    .clickable { viewModel.doiIcon(icon) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(icon, fontSize = 22.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // --- CHỌN MÀU ---
                        Text("Chọn màu sắc", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(DanhSachMau) { hex ->
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(android.graphics.Color.parseColor(hex)))
                                        .border(if (state.colorHex == hex) 3.dp else 0.dp, Color.White, CircleShape)
                                        .clickable { viewModel.doiMau(hex) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card Trạng thái Lưu trữ (Ẩn ví)
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
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
                            onCheckedChange = viewModel::anVi,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF4CAF50)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // NÚT XÓA VÍ
                Button(
                    onClick = { hienThiXacNhanXoa = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F)
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
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
