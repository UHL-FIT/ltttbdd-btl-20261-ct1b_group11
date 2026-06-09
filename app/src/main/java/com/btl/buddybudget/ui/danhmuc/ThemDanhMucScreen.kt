package com.btl.buddybudget.ui.danhmuc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
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
import com.btl.buddybudget.data.icon.TongHopIcon.DanhSachIconChi

import com.btl.buddybudget.data.icon.TongHopIcon.DanhSachMau

@Composable
fun AddCategoryScreen(
    onBack: () -> Unit,
    viewModel: ThemDanhMucViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.thongBaoLoi) {
        uiState.thongBaoLoi?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    // Xử lý sau khi lưu thành công
    LaunchedEffect(uiState.daLuuThanhCong) {
        if (uiState.daLuuThanhCong) {
            viewModel.resetTrangThaiLuu()
            onBack()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // --- NÚT QUAY VỀ HÌNH TRÒN ---
        Box(
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onBack() }
                .align(Alignment.TopStart),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "‹",
                color = MaterialTheme.colorScheme.primary,
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
            // Tiêu đề căn giữa
            Text(
                text = "Nhóm mới",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 28.dp, bottom = 20.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
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
                        placeholder = { Text("Tên nhóm", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                // Loại giao dịch
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Loại giao dịch", color = MaterialTheme.colorScheme.onSurface)
                    Row(modifier = Modifier.clip(RoundedCornerShape(25.dp)).background(MaterialTheme.colorScheme.background).padding(4.dp)) {
                        val isExp = uiState.loaiGiaoDich == KieuGiaoDich.EXPENSE
                        TabItem("Khoản chi", isExp) { viewModel.capNhatLoai(KieuGiaoDich.EXPENSE) }
                        TabItem("Khoản thu", !isExp) { viewModel.capNhatLoai(KieuGiaoDich.INCOME) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Chọn Icon
            Text("Chọn biểu tượng", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(DanhSachIconChi) { icon ->
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .background(if (uiState.iconChon == icon) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                            .border(if (uiState.iconChon == icon) 2.dp else 0.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable { viewModel.capNhatIcon(icon) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(icon, fontSize = 22.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Chọn Màu
            Text("Chọn màu sắc", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(DanhSachMau) { hex ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(android.graphics.Color.parseColor(hex)))
                            .border(if (uiState.mauChon == hex) 3.dp else 0.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .clickable { viewModel.capNhatMau(hex) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = viewModel::luuDanhMuc,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Lưu", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp)
        )
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text, 
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

