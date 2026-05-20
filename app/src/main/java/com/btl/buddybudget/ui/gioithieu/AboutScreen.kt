package com.btl.buddybudget.ui.gioithieu

import androidx.compose.foundation.clickable
import androidx.navigation.NavController
import com.btl.buddybudget.ui.home.Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.btl.buddybudget.R

@Composable
fun AboutScreen(
    navController : NavController,
    onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) { // Thêm nền đen đồng bộ app

        // --- NÚT QUAY VỀ HÌNH TRÒN (Góc trái trên cùng) ---
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
            Text(
                text = "Giới thiệu",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 28.dp, bottom = 20.dp)
            )

            // --- CỤM 1: LOGO & TÊN APP ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(22.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "BudgetBuddy", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Phiên bản 1.0", color = Color(0xFF8E8E93), fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- CỤM 2: DANH SÁCH CHỨC NĂNG ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF1C1C1E))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Khi ấn vào sẽ điều hướng thẳng tới màn hình Danh Mục
                            navController.navigate(Screen.Category.route)
                        }
                ) {
                    InfoRowTextOnly(title = "Danh Mục")
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color(0xFF38383A))
                InfoRowTextOnly(title = "Thống kê chi tiêu")
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color(0xFF38383A))
                InfoRowTextOnly(title = "Cảnh báo ngân sách")
            }
            Spacer(modifier = Modifier.height(40.dp))

            // --- NÚT HƯỚNG DẪN SỬ DỤNG ---
            Button(
                onClick = { /* Xử lý hướng dẫn */ },
                modifier = Modifier
                    .height(46.dp)
                    .widthIn(min = 180.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1C1E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Hướng dẫn sử dụng ›", color = Color(0xFF0A84FF), fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun InfoRowTextOnly(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.White, fontSize = 16.sp)
    }
}