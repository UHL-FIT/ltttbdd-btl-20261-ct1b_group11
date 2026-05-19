package com.btl.buddybudget.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homescreen() {
    // 1. Khởi tạo bộ điều hướng Navigation
    val navController = rememberNavController()

    // 2. Lấy thông tin màn hình hiện tại đang hiển thị để đổi màu nút tương ứng
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            Surface(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    .height(70.dp)
                    .fillMaxWidth(),
                color = Color(0xFF1A1A1A).copy(alpha = 0.5f),
                tonalElevation = 8.dp,
                shape = RoundedCornerShape(35.dp)
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    windowInsets = WindowInsets(0, 0, 0, 0)
                ) {
                    // TAB TỔNG QUAN
                    NavigationBarItem(
                        selected = currentRoute == Screen.Overview.route,
                        onClick = {
                            if (currentRoute != Screen.Overview.route) {
                                navController.navigate(Screen.Overview.route)
                            }
                        },
                        icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Tổng quan", modifier = Modifier.size(32.dp)) },
                        label = { Text("Tổng quan", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50), selectedTextColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )

                    // TAB THỐNG KÊ
                    NavigationBarItem(
                        selected = currentRoute == Screen.Statistics.route,
                        onClick = {
                            if (currentRoute != Screen.Statistics.route) {
                                navController.navigate(Screen.Statistics.route)
                            }
                        },
                        icon = { Icon(imageVector = Icons.Default.List, contentDescription = "Thống kê", modifier = Modifier.size(32.dp)) },
                        label = { Text("Thống kê", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50), selectedTextColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )

                    // NÚT GIỮA GIẢ LẬP KHOẢNG TRỐNG CHO FAB
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = {},
                        enabled = false
                    )

                    // TAB NGÂN SÁCH
                    NavigationBarItem(
                        selected = currentRoute == Screen.Budget.route,
                        onClick = {
                            if (currentRoute != Screen.Budget.route) {
                                navController.navigate(Screen.Budget.route)
                            }
                        },
                        icon = { Icon(imageVector = Icons.Default.Refresh, contentDescription = "Ngân sách", modifier = Modifier.size(32.dp)) },
                        label = { Text("Ngân sách", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50), selectedTextColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )

                    // TAB TÀI KHOẢN
                    NavigationBarItem(
                        selected = currentRoute == Screen.Profile.route,
                        onClick = {
                            if (currentRoute != Screen.Profile.route) {
                                navController.navigate(Screen.Profile.route)
                            }
                        },
                        icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Tài khoản", modifier = Modifier.size(32.dp)) },
                        label = { Text("Tài khoản", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50), selectedTextColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.offset(y = (65).dp),
                onClick = {
                    // Chuyển hẳn sang màn hình nhập giao dịch mới
                    navController.navigate(Screen.AddTransaction.route)
                },
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                shape = RoundedCornerShape(50.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm mới", modifier = Modifier.size(28.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->

        // 3. Sử dụng NavHost làm "vùng chứa" để chuyển đổi các màn hình chính thức
        NavHost(
            navController = navController,
            startDestination = Screen.Overview.route, // Màn hình mặc định khi mở app
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
        ) {
            // Cấu hình định tuyến cho từng Route
            composable(Screen.Overview.route) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    item { MyWallet() } // Gọi hàm từ file HomeScreenComponents của b
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }

            composable(Screen.Statistics.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Màn hình Thống kê", color = Color.White, modifier = Modifier.padding(16.dp))
                }
            }

            composable(Screen.Budget.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Màn hình Ngân sách", color = Color.White, modifier = Modifier.padding(16.dp))
                }
            }

            composable(Screen.Profile.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Màn hình Tài khoản", color = Color.White, modifier = Modifier.padding(16.dp))
                }
            }

            composable(Screen.AddTransaction.route) {
                // Màn hình Thêm giao dịch riêng biệt hoàn toàn (Sẽ ẩn menu đáy đi vì không nằm trong bottomBar)
                Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {
                    Text("Màn hình THÊM GIAO DỊCH MỚI (Bấm Back hệ thống để quay lại)", color = Color.White, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}