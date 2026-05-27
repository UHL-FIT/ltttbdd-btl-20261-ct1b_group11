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

@Composable
fun AddCategoryScreen(
    onBack: () -> Unit,
    viewModel: ThemDanhMucViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val backgroundColor = Color.Black
    val cardColor = Color(0xFF1C1C1E)

    // Xử lý sau khi lưu thành công
    LaunchedEffect(uiState.daLuuThanhCong) {
        if (uiState.daLuuThanhCong) {
            viewModel.resetTrangThaiLuu()
            onBack()
        }
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiêu đề căn giữa (Giống hệt AboutScreen)
            Text(
                text = "Nhóm mới",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 28.dp, bottom = 20.dp)
            )

            // Bỏ Spacer thừa ở đây để các cụm phía dưới đẩy lên đúng vị trí
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

                // Loại giao dịch
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

            // Chọn Icon
            Text("Chọn biểu tượng", color = Color.Gray, modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(DanhSachIcons) { icon ->
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

            // Chọn Màu
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
                onClick = viewModel::luuDanhMuc,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2E)),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Lưu", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0xFF48484A) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = if (isSelected) Color.White else Color.Gray, fontSize = 13.sp)
    }
}
