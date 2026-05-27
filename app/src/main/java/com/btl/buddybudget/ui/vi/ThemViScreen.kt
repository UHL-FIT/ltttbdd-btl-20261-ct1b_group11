package com.btl.buddybudget.ui.vi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import com.btl.buddybudget.ui.danhmuc.DanhSachIcons
import com.btl.buddybudget.ui.danhmuc.DanhSachMau
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.graphics.toColorInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemViScreen(
    onBack: () -> Unit,
    viewModel: ThemViViewModel
) {
    val state = viewModel.uiState

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
                .clickable { viewModel.taoVi(onBack) }
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
                text = "Thêm ví",
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
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1C1C1E)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
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
                                label = { Text("Đơn vị tiền") },
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
                                        .background(Color(hex.toColorInt()))
                                        .border(if (state.colorHex == hex) 3.dp else 0.dp, Color.White, CircleShape)
                                        .clickable { viewModel.doiMau(hex) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1C1C1E)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Lưu trữ ví",
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
                }

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

