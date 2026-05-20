package com.btl.buddybudget.ui.danhmuc

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    onBack: () -> Unit
) {
    val backgroundColor = Color(0xFF000000) // Nền đen đồng bộ app
    val cardColor = Color(0xFF1C1C1E)      // Màu khối xám bo tròn
    var categoryName by remember { mutableStateOf("") }
    var isExpense by remember { mutableStateOf(true) } // Mặc định là Khoản chi

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Nhóm mới", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(36.dp)
                            .background(Color(0xFF2C2C2E), CircleShape)
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = backgroundColor)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- KHỐI NHẬP LIỆU CHÍNH ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardColor)
                    .padding(16.dp)
            ) {
                // Hàng 1: Icon mặc định trái tim + Ô gõ tên nhóm
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFF2C2C2E), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("❤️", fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    TextField(
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        placeholder = { Text("Tên nhóm", color = Color.Gray, fontSize = 16.sp) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFF2C2C2E))

                // Hàng 2: Nút chọn Khoản chi / Khoản thu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Loại giao dịch", color = Color.White, fontSize = 15.sp)

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF2C2C2E))
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isExpense) Color(0xFF48484A) else Color.Transparent)
                                .clickable { isExpense = true }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Khoản chi", color = Color.White, fontSize = 13.sp)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (!isExpense) Color(0xFF48484A) else Color.Transparent)
                                .clickable { isExpense = false }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Khoản thu", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- NÚT LƯU ---
            Button(
                onClick = { /* Tạm thời quay về, bài sau mình kết nối DB lưu nhé */ onBack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2E)),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Lưu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}