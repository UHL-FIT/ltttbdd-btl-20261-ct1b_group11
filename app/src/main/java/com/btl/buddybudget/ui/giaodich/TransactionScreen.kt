package com.btl.buddybudget.ui.giaodich

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.core.graphics.toColorInt
import com.btl.buddybudget.data.db.entities.Vi
import com.btl.buddybudget.data.db.quanhe.GiaoDichvaDanhMuc
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel,
    onNavigateToSearch: () -> Unit,
    onEditTransaction: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val currencyFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.forLanguageTag("vi-VN")))

    if (uiState.isDatePickerVisible) {
        MonthYearPickerDialog(
            currentMonth = uiState.selectedMonth,
            currentYear = uiState.selectedYear,
            onDismiss = { viewModel.toggleDatePicker(false) },
            onConfirm = { month, year ->
                viewModel.onDateSelected(month, year)
            }
        )
    }

    if (uiState.isWalletPickerVisible) {
        WalletPickerDialog(
            wallets = uiState.wallets,
            selectedWalletId = uiState.selectedWalletId,
            onDismiss = { viewModel.toggleWalletPicker(false) },
            onWalletSelected = { id, name -> viewModel.onWalletSelected(id, name) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF2C2C2E),
                            modifier = Modifier.clickable { viewModel.toggleWalletPicker(true) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🌐 ", fontSize = 14.sp)
                                Text(
                                    text = uiState.walletName,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(" ↕", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // KHU VỰC SỐ DƯ
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Số dư", color = Color.Gray, fontSize = 14.sp)
                Text(
                    "${currencyFormat.format(uiState.totalBalance)} đ",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // BỘ CHỌN THÁNG
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleDatePicker(true) }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tháng ${uiState.selectedMonth}, ${uiState.selectedYear}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // CARD BÁO CÁO TỔNG QUAN
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tiền vào", color = Color.Gray)
                                Text(
                                    currencyFormat.format(uiState.incomeAmount),
                                    color = Color(0xFF00BCD4)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tiền ra", color = Color.Gray)
                                Text(
                                    currencyFormat.format(uiState.expenseAmount),
                                    color = Color(0xFFFF5252)
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = Color.DarkGray,
                                thickness = 0.5.dp
                            )
                            Text(
                                text = currencyFormat.format(uiState.incomeAmount - uiState.expenseAmount),
                                color = Color.White,
                                modifier = Modifier.align(Alignment.End),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // DANH SÁCH GIAO DỊCH NHÓM THEO CATEGORY
                uiState.groupedTransactions.forEach { (categoryName, transactions) ->
                    val totalCategory = transactions.sumOf {
                        if (it.giaodich.type == "EXPENSE") -it.giaodich.amount else it.giaodich.amount
                    }
                    val firstGd = transactions.firstOrNull()

                    item {
                        CategoryHeader(
                            categoryName = categoryName,
                            transactionCount = transactions.size,
                            totalAmount = totalCategory,
                            iconColor = firstGd?.danhmuc?.colorHex ?: "#808080",
                            iconName = firstGd?.danhmuc?.iconName ?: "📁"
                        )
                    }

                    items(transactions) { transaction ->
                        TransactionItemRow(
                            item = transaction,
                            onClick = { onEditTransaction(transaction.giaodich.id) },
                            onDelete = { viewModel.deleteTransaction(transaction) }
                        )
                    }
                    
                    item { Spacer(modifier = Modifier.height(12.dp)) }
                }
            }
        }
    }
}

@Composable
fun CategoryHeader(
    categoryName: String,
    transactionCount: Int,
    totalAmount: Double,
    iconColor: String,
    iconName: String
) {
    val currencyFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.forLanguageTag("vi-VN")))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = try { Color(iconColor.toColorInt()).copy(alpha = 0.2f) } catch (e: Exception) { Color.Gray.copy(alpha = 0.2f) }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(iconName, fontSize = 20.sp)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(categoryName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("$transactionCount giao dịch", color = Color.Gray, fontSize = 12.sp)
        }
        Text(
            text = currencyFormat.format(totalAmount),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun TransactionItemRow(
    item: GiaoDichvaDanhMuc,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val currencyFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.forLanguageTag("vi-VN")))
    val locale = Locale.forLanguageTag("vi-VN")
    val sdf = SimpleDateFormat("dd MMMM yyyy, EEEE", locale)
    val dateLabel = sdf.format(Date(item.giaodich.date))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFF2C2C2E), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(item.vi?.iconName ?: "👛", fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dateLabel,
                color = Color.Gray,
                fontSize = 12.sp
            )
            if (item.giaodich.note.isNotEmpty()) {
                Text(
                    text = item.giaodich.note,
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = currencyFormat.format(item.giaodich.amount),
                color = if (item.giaodich.type == "EXPENSE") Color(0xFFFF5252) else Color(0xFF00BCD4),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun MonthYearPickerDialog(
    currentMonth: Int,
    currentYear: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var selectedMonth by remember { mutableIntStateOf(currentMonth) }
    var selectedYear by remember { mutableIntStateOf(currentYear) }

    val months = listOf(
        "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
        "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8",
        "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Chọn thời gian",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { selectedYear-- }) {
                        Text("<", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = selectedYear.toString(),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { selectedYear++ }) {
                        Text(">", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(200.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(12) { index ->
                        val month = index + 1
                        val isSelected = month == selectedMonth
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .aspectRatio(1.5f)
                                .background(
                                    if (isSelected) Color(0xFF4CAF50) else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedMonth = month },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = months[index],
                                color = if (isSelected) Color.White else Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy", color = Color.Gray)
                    }
                    TextButton(onClick = { onConfirm(selectedMonth, selectedYear) }) {
                        Text("Chọn", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun WalletPickerDialog(
    wallets: List<Vi>,
    selectedWalletId: Int?,
    onDismiss: () -> Unit,
    onWalletSelected: (Int?, String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1A1A1A),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Chọn tài khoản",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxHeight(0.5f)
                ) {
                    item {
                        WalletItem(
                            name = "Tổng cộng",
                            isSelected = selectedWalletId == null,
                            onClick = { onWalletSelected(null, "Tổng cộng") }
                        )
                    }

                    items(wallets) { wallet ->
                        WalletItem(
                            name = wallet.name,
                            isSelected = wallet.id == selectedWalletId,
                            onClick = { onWalletSelected(wallet.id, wallet.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Đóng", color = Color(0xFF4CAF50))
                }
            }
        }
    }
}

@Composable
fun WalletItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            color = if (isSelected) Color(0xFF4CAF50) else Color.White,
            fontSize = 16.sp
        )
        if (isSelected) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4CAF50))
        }
    }
}
