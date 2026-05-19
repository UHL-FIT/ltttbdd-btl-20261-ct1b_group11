package com.btl.buddybudget.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homescreen(){
    var selectedTab by remember { mutableIntStateOf(0) }

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
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }, // Khi bấm vào, đổi trạng thái sang nút số 0
                        icon = { Icon(imageVector = Icons.Default.Home,contentDescription = "Tổng quan",
                            modifier = Modifier.size(32.dp)) },
                        label = { Text("Tổng quan", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50),
                            selectedTextColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(imageVector = Icons.Default.List, contentDescription = "Thống kê",
                            modifier = Modifier.size(32.dp)) },
                        label = { Text("Thống kê", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50), selectedTextColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = {},
                    enabled = false
                )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = {selectedTab == 2},
                        icon = { Icon(imageVector = Icons.Default.Refresh,contentDescription = "Ngân sách",
                            modifier = Modifier.size(32.dp))},
                        label = { Text("Ngân sách",fontSize = 11.sp)},
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50), selectedTextColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Tài khoản",
                            modifier = Modifier.size(32.dp))},
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
                modifier = Modifier.offset(y=(65).dp),
                onClick = { /* Handle FAB click */ },
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                shape = RoundedCornerShape(50.dp)
            ){
                Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm mới", modifier = Modifier.size(28.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding))

    }
}