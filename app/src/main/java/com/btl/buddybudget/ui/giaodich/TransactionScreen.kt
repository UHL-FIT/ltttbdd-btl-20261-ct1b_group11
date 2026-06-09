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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Check

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
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.draw.clip
import androidx.core.graphics.toColorInt
import com.btl.buddybudget.data.db.TransactionViewMode
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

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // --- NÚT TÌM KIẾM & MENU 3 CHẤM (Góc phải trên cùng) ---
        Row(
            modifier = Modifier
                .padding(end = 16.dp, top = 20.dp)
                .align(Alignment.TopEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onNavigateToSearch() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))

            var showMenu by remember { mutableStateOf(false) }
            Box {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { showMenu = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
                DropdownMenu(
                    shape = RoundedCornerShape(20.dp),
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    DropdownMenuItem(
                        text = { Text("Xem theo danh mục") },
                        leadingIcon = { Icon(Icons.Default.List, null) },
                        onClick = {
                            viewModel.onChangeViewMode(TransactionViewMode.BY_CATEGORY)
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Xem theo ngày") },
                        leadingIcon = { Icon(Icons.Default.DateRange, null) },
                        onClick = {
                            viewModel.onChangeViewMode(TransactionViewMode.BY_DATE)
                            showMenu = false
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header / Wallet Picker
            Box(
                modifier = Modifier
                    .padding(top = 28.dp, bottom = 12.dp)
                    .clickable { viewModel.toggleWalletPicker(true) }
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🌐 ", fontSize = 14.sp)
                        Text(
                            text = uiState.walletName,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(" ↕", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                }
            }

            // KHU VỰC SỐ DƯ
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Số dư", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                Text(
                    "${currencyFormat.format(uiState.totalBalance)} đ",
                    color = MaterialTheme.colorScheme.onBackground,
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
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // CARD BÁO CÁO TỔNG QUAN
                item {
                    Card(
                        shape = RoundedCornerShape(25.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tiền vào", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    "+"+currencyFormat.format(uiState.incomeAmount)+"đ",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tiền ra", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    "-"+currencyFormat.format(uiState.expenseAmount)+"đ",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = MaterialTheme.colorScheme.outlineVariant,
                                thickness = 0.5.dp
                            )
                            Text(
                                text = currencyFormat.format(uiState.incomeAmount - uiState.expenseAmount)+"đ",
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.align(Alignment.End),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // DANH SÁCH GIAO DỊCH
                uiState.groupedTransactions.forEach { (headerName, transactions) ->
                    val totalAmount = transactions.sumOf {
                        if (it.giaodich.type == "EXPENSE") -it.giaodich.amount else it.giaodich.amount
                    }
                    val firstGd = transactions.firstOrNull()

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                if (uiState.viewMode == TransactionViewMode.BY_CATEGORY) {
                                    CategoryHeader(
                                        categoryName = headerName,
                                        transactionCount = transactions.size,
                                        totalAmount = totalAmount,
                                        iconColor = firstGd?.danhmuc?.colorHex ?: "#808080",
                                        iconName = firstGd?.danhmuc?.iconName ?: "📁"
                                    )
                                } else {
                                    DateHeader(
                                        dateLabel = headerName,
                                        transactionCount = transactions.size,
                                        totalAmount = totalAmount
                                    )
                                }

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    thickness = 1.dp
                                )

                                // Danh sách giao dịch bên trong
                                transactions.forEachIndexed { index, transaction ->
                                    TransactionItemRow(
                                        item = transaction,
                                        viewMode = uiState.viewMode,
                                        onClick = { onEditTransaction(transaction.giaodich.id) }
                                    )
                                    if (index < transactions.size - 1) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 40.dp, vertical = 4.dp),
                                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                            thickness = 0.5.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
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
            Text(categoryName, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("$transactionCount giao dịch", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        Text(
            text = currencyFormat.format(totalAmount)+"đ",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun DateHeader(
    dateLabel: String,
    transactionCount: Int,
    totalAmount: Double
) {
    val currencyFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.forLanguageTag("vi-VN")))
    
    // Convert dd/MM/yyyy to a more readable format like "15 Tháng 03"
    val readableDate = try {
        val inputSdf = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("vi-VN"))
        val outputSdf = SimpleDateFormat("dd MMMM", Locale.forLanguageTag("vi-VN"))
        val date = inputSdf.parse(dateLabel)
        outputSdf.format(date ?: Date())
    } catch (e: Exception) {
        dateLabel
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(readableDate, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("$transactionCount giao dịch", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        Text(
            text = currencyFormat.format(totalAmount)+"đ",
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun TransactionItemRow(
    item: GiaoDichvaDanhMuc,
    viewMode: TransactionViewMode = TransactionViewMode.BY_CATEGORY,
    onClick: () -> Unit
) {
    val currencyFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.forLanguageTag("vi-VN")))
    val locale = Locale.forLanguageTag("vi-VN")
    
    // Label phụ (Dưới note): 
    // Nếu xem theo Danh mục -> Hiện ngày
    // Nếu xem theo Ngày -> Hiện tên danh mục
    val subLabel = if (viewMode == TransactionViewMode.BY_CATEGORY) {
        val sdf = SimpleDateFormat("dd MMMM yyyy, EEEE", locale)
        sdf.format(Date(item.giaodich.date))
    } else {
        item.danhmuc?.name ?: "Khác"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon chính:
        // Nếu xem theo Danh mục -> Hiện icon Ví
        // Nếu xem theo Ngày -> Hiện icon Danh mục
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(item.vi?.colorHex?.toColorInt() ?: 0xFF808080.toInt()), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (viewMode == TransactionViewMode.BY_CATEGORY) {
                Text(item.vi?.iconName ?: "👛", fontSize = 16.sp)
            } else {
                Text(item.danhmuc?.iconName ?: "📁", fontSize = 16.sp)
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = subLabel,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
            Text(
                text = if (item.giaodich.note.isNotEmpty()) item.giaodich.note else "Không có ghi chú",
                color = if (item.giaodich.note.isNotEmpty()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                maxLines = 1,
                fontWeight = if (item.giaodich.note.isNotEmpty()) FontWeight.Medium else FontWeight.Normal
            )
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (item.giaodich.type == "EXPENSE") "-" else "+"}${currencyFormat.format(item.giaodich.amount)} đ",
                color = if (item.giaodich.type == "EXPENSE") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            // Nếu xem theo Ngày, hiện thêm icon ví nhỏ bên dưới số tiền để biết tiền từ ví nào
            if (viewMode == TransactionViewMode.BY_DATE) {
                Text(
                    text = "${item.vi?.iconName ?: "👛"} ${item.vi?.name ?: ""}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
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
                    color = MaterialTheme.colorScheme.onSurface,
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
                        Text("<", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = selectedYear.toString(),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { selectedYear++ }) {
                        Text(">", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
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
                                    if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedMonth = month },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = months[index],
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
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
                        Text("Hủy", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    TextButton(onClick = { onConfirm(selectedMonth, selectedYear) }) {
                        Text("Chọn", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
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
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Chọn tài khoản",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp)
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
                    Text("Đóng", color = MaterialTheme.colorScheme.primary)
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
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )
        if (isSelected) {
            Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}
