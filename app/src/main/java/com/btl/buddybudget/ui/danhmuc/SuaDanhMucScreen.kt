package com.btl.buddybudget.ui.danhmuc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.btl.buddybudget.data.db.KieuGiaoDich
import androidx.compose.foundation.lazy.items // Rất quan trọng để sửa lỗi chữ items
import com.btl.buddybudget.data.icon.TongHopIcon.DanhSachIconChi
import com.btl.buddybudget.data.icon.TongHopIcon.DanhSachMau

@Composable
fun EditCategoryScreen(
    idDanhMuc: Int,
    onBack: () -> Unit,
    viewModel: SuaDanhMucViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val backgroundColor = Color.Black
    val cardColor = Color(0xFF1C1C1E)

    var hienThiXacNhanXoa by remember { mutableStateOf(false) }

    LaunchedEffect(idDanhMuc) {
        viewModel.loadDanhMuc(idDanhMuc)
    }

    LaunchedEffect(uiState.daCapNhat, uiState.daXoa) {
        if (uiState.daCapNhat || uiState.daXoa) {
            onBack()
        }
    }

    if (hienThiXacNhanXoa) {
        AlertDialog(
            onDismissRequest = { hienThiXacNhanXoa = false },
            title = { Text("Xóa nhóm", color = Color.White) },
            text = { Text("Bạn có chắc chắn muốn xóa nhóm '${uiState.tenDanhMuc}' này không?", color = Color.LightGray) },
            containerColor = Color(0xFF1E1E1E),
            confirmButton = {
                TextButton(onClick = { viewModel.xoaDanhMuc() }) {
                    Text("XÓA", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { hienThiXacNhanXoa = false }) {
                    Text("HỦY", color = Color.Gray)
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        // --- NÚT QUAY VỀ HÌNH TRÒN (Giống AboutScreen) ---
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

        // --- NÚT XÓA (Góc phải trên cùng) ---
        if (!uiState.isDefault && !uiState.isLoading) {
            Box(
                modifier = Modifier
                    .padding(end = 20.dp, top = 20.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1C1C1E))
                    .clickable { hienThiXacNhanXoa = true }
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Xóa",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiêu đề căn giữa (Giống AboutScreen)
            Text(
                text = "Sửa nhóm",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 28.dp, bottom = 20.dp)
            )

            if (uiState.isLoading) {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(color = Color.White)
                Spacer(modifier = Modifier.weight(1f))
            } else {
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardColor)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color(android.graphics.Color.parseColor(uiState.mauChon)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(uiState.iconChon, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        TextField(
                            value = uiState.tenDanhMuc,
                            onValueChange = viewModel::capNhatTen,
                            placeholder = { Text("Tên nhóm", color = Color.Gray) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFF2C2C2E))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Loại giao dịch", color = Color.White)
                        Row(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xFF2C2C2E)).padding(4.dp)) {
                            val isExp = uiState.loaiGiaoDich == KieuGiaoDich.EXPENSE
                            TabItem("Khoản chi", isExp) { viewModel.capNhatLoai(KieuGiaoDich.EXPENSE) }
                            TabItem("Khoản thu", !isExp) { viewModel.capNhatLoai(KieuGiaoDich.INCOME) }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text("Chọn biểu tượng", color = Color.Gray, modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(DanhSachIconChi) { icon ->
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .background(if (uiState.iconChon == icon) Color.White.copy(0.2f) else Color(0xFF2C2C2E))
                                .border(if (uiState.iconChon == icon) 2.dp else 0.dp, Color.White, CircleShape)
                                .clickable { viewModel.capNhatIcon(icon) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(icon, fontSize = 22.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text("Chọn màu sắc", color = Color.Gray, modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(DanhSachMau) { hex ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(android.graphics.Color.parseColor(hex)))
                                .border(if (uiState.mauChon == hex) 3.dp else 0.dp, Color.White, CircleShape)
                                .clickable { viewModel.capNhatMau(hex) }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (uiState.thongBaoLoi != null) {
                    Text(uiState.thongBaoLoi!!, color = Color.Red, fontSize = 14.sp)
                }

                Button(
                    onClick = viewModel::luuCapNhat,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2E)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Lưu thay đổi", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
