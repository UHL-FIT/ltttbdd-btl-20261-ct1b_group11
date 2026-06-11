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

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.btl.buddybudget.data.icon.TongHopIcon.DanhSachIconVi
import com.btl.buddybudget.data.icon.TongHopIcon.DanhSachMau

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuaViScreen(
    walletId: Int,
    onBack: () -> Unit,
    viewModel: SuaViViewModel
) {
    val state = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(walletId) {
        viewModel.loadWallet(walletId)
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            launch { snackbarHostState.showSnackbar(it) }
            delay(600)
            viewModel.clearSuccessMessage()
            onBack()
        }
    }

    var hienThiXacNhanXoa by remember { mutableStateOf(false) }

    // Hộp thoại cảnh báo khi nhấn nút Xóa
    if (hienThiXacNhanXoa) {
        AlertDialog(
            onDismissRequest = { hienThiXacNhanXoa = false },
            title = { Text(text = "Xóa ví", color = MaterialTheme.colorScheme.onSurface) },
            text = { Text(text = "Bạn có chắc chắn muốn xóa ví '${state.name}' không? Hành động này không thể hoàn tác.", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            confirmButton = {
                TextButton(
                    onClick = {
                        hienThiXacNhanXoa = false
                        viewModel.xoaVi()
                    }
                ) {
                    Text("XÓA", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { hienThiXacNhanXoa = false }) {
                    Text("HỦY", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
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

        // --- NÚT XÓA (Góc phải trên cùng) ---
        Box(
            modifier = Modifier
                .padding(end = 20.dp, top = 20.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { hienThiXacNhanXoa = true }
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Xóa",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(20.dp)
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
                text = "Sửa ví",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 28.dp, bottom = 20.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        OutlinedTextField(
                            value = state.name,
                            onValueChange = viewModel::doiTenVi,
                            label = { Text("Tên ví") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        /*
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = state.soDu,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Số dư hiện tại") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                            )
                        )
                         */

                        Spacer(modifier = Modifier.height(16.dp))

                        var expandedDonVi by remember { mutableStateOf(false) }
                        val listDonVi = listOf("VND")

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
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                listDonVi.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption, color = MaterialTheme.colorScheme.onSurface) },
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
                    Text("Chọn biểu tượng", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(DanhSachIconVi) { icon ->
                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .clip(CircleShape)
                                    .background(if (state.iconName == icon) MaterialTheme.colorScheme.primary.copy(0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                                    .border(if (state.iconName == icon) 2.dp else 0.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                    .clickable { viewModel.doiIcon(icon) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(icon, fontSize = 22.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // --- CHỌN MÀU ---
                        Text("Chọn màu sắc", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(DanhSachMau) { hex ->
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(android.graphics.Color.parseColor(hex)))
                                        .border(if (state.colorHex == hex) 3.dp else 0.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                        .clickable { viewModel.doiMau(hex) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card Trạng thái Lưu trữ (Ẩn ví)
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
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
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = state.isArchived,
                            onCheckedChange = viewModel::anVi,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- NÚT LƯU CỐ ĐỊNH PHÍA DƯỚI ---
            Button(
                onClick = { viewModel.capNhatVi() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Lưu thay đổi", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}
