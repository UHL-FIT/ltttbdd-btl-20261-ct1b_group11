package com.btl.buddybudget.ui.danhmuc

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.btl.buddybudget.data.db.KieuGiaoDich
import com.btl.buddybudget.data.db.entities.DanhMuc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DanhMucScreen(onBack: () -> Unit) { // ◄ Nhận sự kiện onBack từ file cha truyền xuống

    // --- CẤU HÌNH STATUS BAR TRẮNG ---
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val currentType = if (selectedTab == 0) KieuGiaoDich.EXPENSE else KieuGiaoDich.INCOME

    val danhMucList = remember {
        mutableStateListOf(
            DanhMuc(1, "Ăn uống", "ic_food", "#FF5252", KieuGiaoDich.EXPENSE, isDefault = true),
            DanhMuc(2, "Hoá đơn", "ic_bill", "#546E7A", KieuGiaoDich.EXPENSE, isDefault = true),
            DanhMuc(3, "Lương", "ic_salary", "#4CAF50", KieuGiaoDich.INCOME, isDefault = true),
            DanhMuc(4, "Mua sắm", "ic_shop", "#EC407A", KieuGiaoDich.EXPENSE, isDefault = false)
        )
    }

    val filteredItems = danhMucList.filter { it.type == currentType }

    val backgroundColor = Color(0xFF1C1C1E)
    val cardColor = Color(0xFF2C2C2E)
    val primaryGreen = Color(0xFF4CAF50)
    val grayCircle = Color(0xFF3A3A3C)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("DANH MỤC", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(36.dp)
                            .background(grayCircle, CircleShape)
                            .clickable { onBack() }, // ◄ ĐÃ FIX SẠCH KÝ TỰ RÁC VÀ GỌI HÀM ONBACK CHUẨN
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = backgroundColor)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            // --- THANH CHỌN HIỆN ĐẠI (Capsule Style) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .height(48.dp)
                    .background(cardColor, RoundedCornerShape(24.dp))
                    .padding(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (selectedTab == 0) Color(0xFF636366) else Color.Transparent)
                            .clickable { selectedTab = 0 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Khoản chi", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (selectedTab == 1) Color(0xFF636366) else Color.Transparent)
                            .clickable { selectedTab = 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Khoản thu", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nút Nhóm mới - Bo tròn Capsule
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
                    .background(cardColor, RoundedCornerShape(28.dp))
                    .clickable { /* Thêm mới */ }
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.AddCircle, null, tint = primaryGreen)
                Text("  Nhóm mới", color = primaryGreen, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Danh sách hiển thị - Bo tròn Capsule đồng nhất
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredItems) { item ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        color = cardColor,
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        Color(android.graphics.Color.parseColor(item.colorHex)),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(item.name.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            Text(
                                text = item.name,
                                color = Color.White,
                                modifier = Modifier.padding(start = 16.dp).weight(1f),
                                fontSize = 16.sp
                            )

                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                null,
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}