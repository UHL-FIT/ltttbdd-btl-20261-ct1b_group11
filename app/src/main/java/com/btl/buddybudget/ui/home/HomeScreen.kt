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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.btl.buddybudget.AppViewModelFactory
import com.btl.buddybudget.ui.danhmuc.ChonDanhMucScreen
import com.btl.buddybudget.ui.danhmuc.QuanLyDanhMucScreen
import com.btl.buddybudget.ui.danhmuc.AddCategoryScreen
import com.btl.buddybudget.ui.danhmuc.EditCategoryScreen
import com.btl.buddybudget.ui.danhmuc.SuaDanhMucViewModel
import com.btl.buddybudget.ui.danhmuc.DanhMucViewModel
import com.btl.buddybudget.ui.danhmuc.ThemDanhMucViewModel
import com.btl.buddybudget.ui.gioithieu.AboutScreen
import com.btl.buddybudget.ui.vi.ViScreen
import com.btl.buddybudget.ui.vi.SuaViScreen
import com.btl.buddybudget.ui.vi.SuaViViewModel
import com.btl.buddybudget.ui.vi.ChonViScreen
import com.btl.buddybudget.ui.giaodich.SearchTransactionsScreen
import com.btl.buddybudget.ui.giaodich.SuaGiaoDichScreen
import com.btl.buddybudget.ui.giaodich.SuaGiaoDichViewModel
import com.btl.buddybudget.ui.giaodich.ThemGiaoDichScreen
import com.btl.buddybudget.ui.giaodich.ThemGiaoDichViewModel
import com.btl.buddybudget.ui.giaodich.TransactionScreen
import com.btl.buddybudget.ui.giaodich.TransactionViewModel
import com.btl.buddybudget.ui.thongke.ThongKeScreen
import com.btl.buddybudget.ui.vi.ThemViScreen
import com.btl.buddybudget.ui.vi.ThemViViewModel
import com.btl.buddybudget.ui.vi.ViViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModelFactory: AppViewModelFactory) {

    val navController = rememberNavController()
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

                    NavigationBarItem(
                        selected = currentRoute == Screen.TransactionHistory.route,
                        onClick = {
                            if (currentRoute != Screen.TransactionHistory.route) {
                                navController.navigate(Screen.TransactionHistory.route)
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

                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = {},
                        enabled = false
                    )

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

                    NavigationBarItem(
                        selected = currentRoute == Screen.About.route,
                        onClick = {
                            if (currentRoute != Screen.About.route) {
                                navController.navigate(Screen.About.route)
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
        val themGiaoDichViewModel: ThemGiaoDichViewModel = viewModel(factory = viewModelFactory)

        NavHost(
            navController = navController,
            startDestination = Screen.Overview.route,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
        ) {
            composable(Screen.Overview.route) {
                val viViewModel: ViViewModel = viewModel(factory = viewModelFactory)
                val uiState by viViewModel.uiState.collectAsState()
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    item { MyWallet(
                        uiState = uiState,
                        onViewAllClick = {
                            navController.navigate(Screen.Wallet.route)
                        },
                        onTransactionHistoryClick = {
                            navController.navigate(Screen.TransactionHistory.route)
                        }
                    )
                    }
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
            /*
            composable(Screen.Profile.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Màn hình Tài khoản", color = Color.White, modifier = Modifier.padding(16.dp))
                }
            }
            */

            composable(Screen.AddTransaction.route) {
                ThemGiaoDichScreen(
                    viewModel = themGiaoDichViewModel,
                    onCancel = { navController.popBackStack() },
                    onNavigateToSelectGroup = { isExpense ->
                        val type = if (isExpense) "EXPENSE" else "INCOME"
                        navController.navigate(Screen.Category.createRoute(type))
                    },
                    onNavigateToSelectWallet = {
                        navController.navigate(Screen.SelectWallet.route)
                    }
                )
            }

            composable(Screen.TransactionHistory.route) {
                val transactionViewModel: TransactionViewModel = viewModel(factory = viewModelFactory)
                TransactionScreen(
                    viewModel = transactionViewModel,
                    onNavigateToSearch = { navController.navigate(Screen.SearchTransactions.route) },
                    onEditTransaction = { id ->
                        navController.navigate(Screen.EditTransaction.createRoute(id))
                    }
                )
            }

            composable(
                route = Screen.EditTransaction.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getInt("id") ?: 0
                val suaGiaoDichViewModel: SuaGiaoDichViewModel = viewModel(factory = viewModelFactory)
                SuaGiaoDichScreen(
                    idGiaoDich = transactionId,
                    viewModel = suaGiaoDichViewModel,
                    onCancel = { navController.popBackStack() },
                    onNavigateToSelectGroup = { isExpense ->
                        val type = if (isExpense) "EXPENSE" else "INCOME"
                        navController.navigate("category_edit/$type")
                    },
                    onNavigateToSelectWallet = {
                        navController.navigate("select_wallet_edit")
                    }
                )
            }

            composable(
                route = "category_edit/{type}",
                arguments = listOf(navArgument("type") { type = NavType.StringType })
            ) { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type") ?: "EXPENSE"
                val danhMucViewModel: DanhMucViewModel = viewModel(factory = viewModelFactory)
                val suaGiaoDichViewModel: SuaGiaoDichViewModel = viewModel(factory = viewModelFactory)

                com.btl.buddybudget.ui.danhmuc.ChonDanhMucScreen(
                    filterType = type,
                    onBack = { navController.popBackStack() },
                    onSelect = { selectedCate ->
                        suaGiaoDichViewModel.onGroupSelected(
                            selectedCate.id,
                            selectedCate.name,
                            selectedCate.colorHex,
                            selectedCate.iconName
                        )
                        navController.popBackStack()
                    },
                    viewModel = danhMucViewModel
                )
            }

            composable("select_wallet_edit") {
                val viViewModel: ViViewModel = viewModel(factory = viewModelFactory)
                val suaGiaoDichViewModel: SuaGiaoDichViewModel = viewModel(factory = viewModelFactory)
                com.btl.buddybudget.ui.vi.ChonViScreen(
                    viewModel = viViewModel,
                    onBack = { navController.popBackStack() },
                    onSelect = { wallet ->
                        suaGiaoDichViewModel.onWalletSelected(wallet.id, wallet.name)
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.About.route) {
                // Gọi màn hình giới thiệu của bạn lên
                AboutScreen(
                    navController = navController,
                    onBack = { navController.popBackStack()
                    }
                )
            }
            // Màn hình CHỌN danh mục (khi thêm giao dịch)
            composable(
                route = Screen.Category.route,
                arguments = listOf(navArgument("type") { type = NavType.StringType })
            ) { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type") ?: "EXPENSE"
                val danhMucViewModel: DanhMucViewModel = viewModel(factory = viewModelFactory)
                
                com.btl.buddybudget.ui.danhmuc.ChonDanhMucScreen(
                    filterType = type,
                    onBack = { navController.popBackStack() },
                    onSelect = { selectedCate ->
                        themGiaoDichViewModel.onGroupSelected(
                            selectedCate.id, 
                            selectedCate.name, 
                            selectedCate.colorHex,
                            selectedCate.iconName
                        )
                        navController.popBackStack()
                    },
                    viewModel = danhMucViewModel
                )
            }

            // Màn hình QUẢN LÝ danh mục
            composable(Screen.ManageCategory.route) {
                val danhMucViewModel: DanhMucViewModel = viewModel(factory = viewModelFactory)
                com.btl.buddybudget.ui.danhmuc.QuanLyDanhMucScreen(
                    onBack = { navController.popBackStack() },
                    onAddCate = { navController.navigate(Screen.AddCategory.route) },
                    onEditCate = { id -> navController.navigate(Screen.EditCategory.createRoute(id)) },
                    viewModel = danhMucViewModel
                )
            }

            // Màn hình Thêm nhóm mới
            composable(Screen.AddCategory.route) {
                val themDanhMucViewModel: ThemDanhMucViewModel = viewModel(factory = viewModelFactory)
                AddCategoryScreen(
                    onBack = { navController.popBackStack() },
                    viewModel = themDanhMucViewModel
                )
            }

            // Màn hình Sửa nhóm
            composable(
                route = Screen.EditCategory.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getInt("id") ?: 0
                val suaDanhMucViewModel: SuaDanhMucViewModel = viewModel(factory = viewModelFactory)
                EditCategoryScreen(
                    idDanhMuc = categoryId,
                    onBack = { navController.popBackStack() },
                    viewModel = suaDanhMucViewModel
                )
            }

            composable(Screen.Wallet.route) {

                val viViewModel: ViViewModel = viewModel(factory = viewModelFactory)

                ViScreen(
                    viviewModel = viViewModel,
                    onBack = { navController.popBackStack() },
                    onAddWallet = { navController.navigate(Screen.AddWallet.route) },
                    onEditWallet = { id -> navController.navigate(Screen.EditWallet.createRoute(id)) }
                )
            }

            composable(Screen.SelectWallet.route) {
                val viViewModel: ViViewModel = viewModel(factory = viewModelFactory)
                com.btl.buddybudget.ui.vi.ChonViScreen(
                    viewModel = viViewModel,
                    onBack = { navController.popBackStack() },
                    onSelect = { wallet ->
                        themGiaoDichViewModel.onWalletSelected(wallet.id, wallet.name)
                        navController.popBackStack()
                    }
                )
            }


            composable(Screen.AddWallet.route) {
                val themViViewModel: ThemViViewModel = viewModel(factory = viewModelFactory)

                ThemViScreen(
                    viewModel = themViViewModel,
                    onBack = { navController.popBackStack()}
                    //onSuccess={navController.navigate(Screen.Wallet.route)}

                )
            }

            composable(
                route = Screen.EditWallet.route, // Bản chất chuỗi này thường định nghĩa là "edit_wallet/{id}"
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType } // Khai báo tham số nhận vào là kiểu Int
                )
            ) { backStackEntry ->
                // 1. Lấy giá trị id ra từ arguments (khớp với tên "id" cấu hình ở trên)
                val walletId = backStackEntry.arguments?.getInt("id") ?: 0

                // 2. Khởi tạo ViewModel phụ trách sửa ví
                val suaViViewModel: SuaViViewModel = viewModel(factory = viewModelFactory)

                // 3. Gọi màn hình SuaViScreen và truyền id cùng ViewModel vào
                SuaViScreen(
                    walletId = walletId,
                    viewModel = suaViViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.SearchTransactions.route) {
                val transactionViewModel: TransactionViewModel = viewModel(factory = viewModelFactory)
                SearchTransactionsScreen(
                    viewModel = transactionViewModel,
                    onBack = { navController.popBackStack() },
                    onEditTransaction = { id ->
                        navController.navigate(Screen.EditTransaction.createRoute(id))
                    }
                )
            }
        }
    }
}

