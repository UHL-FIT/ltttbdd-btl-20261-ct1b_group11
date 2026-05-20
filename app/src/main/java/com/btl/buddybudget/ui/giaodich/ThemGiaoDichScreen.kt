package com.btl.buddybudget.ui.giaodich

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThemGiaoDichScreen(
    viewModel: ThemGiaoDichViewModel,
    onCancel: () -> Unit,
    onNavigateToSelectGroup: () -> Unit // Sự kiện để bài sau nhấn vào dòng "Chọn nhóm" sẽ nhảy trang
) {
    // Thu thập trạng thái UI từ ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val cardColor = Color(0xFF1C1C1E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- TOP BAR TỰ CHẾ ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            // Nút Huỷ dạng hình Oval bên trái
            Surface(
                onClick = { onCancel() },
                color = Color(0xFF2C2C2E),
                shape = CircleShape,
                modifier = Modifier.size(width = 65.dp, height = 36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Huỷ", color = Color.White, fontSize = 14.sp)
                }
            }

            // Tiêu đề chính giữa
            Text(
                text = "Thêm Giao Dịch",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- KHỐI NHẬP LIỆU CHÍNH ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(cardColor)
                .padding(16.dp)
        ) {
            // 1. Hàng chọn Khoản chi / Khoản thu dạng Capsule
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color(0xFF2C2C2E))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (uiState.idExpense) Color(0xFF48484A) else Color.Transparent)
                        .clickable { viewModel.onTransactionTypeChanged(true) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Khoản chi", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (!uiState.idExpense) Color(0xFF48484A) else Color.Transparent)
                        .clickable { viewModel.onTransactionTypeChanged(false) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Khoản thu", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Hàng chọn ví tài khoản
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Xử lý chọn ví sau này */ }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF4CAF50), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("💳", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = uiState.selectedWalletName, color = Color.White, fontSize = 16.sp)
                }
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
            }

            HorizontalDivider(color = Color(0xFF2C2C2E), modifier = Modifier.padding(vertical = 8.dp))

            // 3. Khối hiển thị nhập số tiền
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text("Số tiền", color = Color.Gray, fontSize = 12.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFF2C2C2E),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text("VND", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }

                    BasicTextField(
                        value = uiState.amount,
                        onValueChange = { viewModel.onAmountChanged(it) },
                        textStyle = TextStyle(color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (uiState.amount.isEmpty()) {
                                Text("0", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            }
                            innerTextField()
                        }
                    )
                }
            }

            HorizontalDivider(color = Color(0xFF2C2C2E), modifier = Modifier.padding(vertical = 8.dp))

            // 4. Hàng Chọn nhóm (Được gán sự kiện để click nhảy sang trang danh mục)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToSelectGroup() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF48484A), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📁", fontSize = 14.sp) // Có thể thay bằng Icon tùy ý b nhé
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = uiState.selectedGroupName,
                        color = if (uiState.selectedGroupName == "Chọn nhóm") Color.Gray else Color.White,
                        fontSize = 16.sp
                    )
                }
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- NÚT LƯU CỐ ĐỊNH PHÍA DƯỚI ---
        Button(
            onClick = {
                viewModel.saveTransaction(onSuccess = { onCancel() })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1C1E)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}