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

/**
 * MÀN HÌNH CHỌN DANH MỤC (Dùng cho luồng Thêm giao dịch)
 * - Tự động lọc theo type (EXPENSE/INCOME)
 * - Không có TabBar
 * - Click vào là chọn và quay về
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChonDanhMucScreen(
    filterType: String,
    onBack: () -> Unit,
    onSelect: (DanhMuc) -> Unit,
    viewModel: DanhMucViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    // --- CẤU HÌNH STATUS BAR TRẮNG ---
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }


    val currentType = if (filterType == "INCOME") KieuGiaoDich.INCOME else KieuGiaoDich.EXPENSE
    val filteredItems = uiState.danhMucs.filter { it.type == currentType }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                // --- NÚT QUAY VỀ HÌNH TRÒN (Đồng bộ style) ---
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
                    val title = if (filterType == "EXPENSE") "Chọn nhóm chi" else "Chọn nhóm thu"
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 28.dp, bottom = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredItems) { item ->
                            CateItem(
                                danhMuc = item,
                                onClick = { onSelect(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * MÀN HÌNH QUẢN LÝ DANH MỤC (Dùng để xem/sửa/xóa)
 * - Có TabBar Chi/Thu
 * - Click vào item để đi tới màn hình Sửa
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuanLyDanhMucScreen(
    onBack: () -> Unit,
    onAddCate: () -> Unit,
    onEditCate: (Int) -> Unit,
    viewModel: DanhMucViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    
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
    val filteredItems = uiState.danhMucs.filter { it.type == currentType }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
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
                        text = "Danh mục",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 28.dp, bottom = 20.dp)
                    )

                    // TabBar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .height(48.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp))
                            .padding(4.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (selectedTab == 0) MaterialTheme.colorScheme.primary else Color.Transparent)
                                    .clickable { selectedTab = 0 },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Khoản chi",
                                    color = if (selectedTab == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (selectedTab == 1) MaterialTheme.colorScheme.primary else Color.Transparent)
                                    .clickable { selectedTab = 1 },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Khoản thu",
                                    color = if (selectedTab == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nút Nhóm mới
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(28.dp))
                            .clickable { onAddCate() }
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.AddCircle, null, tint = MaterialTheme.colorScheme.primary)
                        Text("  Nhóm mới", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredItems) { item ->
                            CateItem(
                                danhMuc = item,
                                onClick = { onEditCate(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CateItem(
    danhMuc: DanhMuc,
    onClick: () -> Unit
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .height(64.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
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
                        Color(android.graphics.Color.parseColor(danhMuc.colorHex)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(danhMuc.iconName, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            Text(
                text = danhMuc.name,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp).weight(1f),
                fontSize = 16.sp
            )

            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
