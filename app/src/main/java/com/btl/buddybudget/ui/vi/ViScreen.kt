package com.btl.buddybudget.ui.vi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.LayoutDirection
import com.btl.buddybudget.data.db.quanhe.WalletWithBalance
import androidx.compose.runtime.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViScreen(
    viviewModel: ViViewModel,
    onBack: () -> Unit,
    onAddWallet: () -> Unit,
    onEditWallet: (Int) -> Unit
) {
    val uiState by viviewModel.uiState.collectAsState()
    var showArchived by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    val filteredWallets = if (showArchived) {
        uiState.wallets.filter { it.isArchived }
    } else {
        uiState.wallets.filter { !it.isArchived }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (showArchived) "Ví đã ẩn" else "Quản lý ví",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Menu", tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            containerColor = Color(0xFF2C2C2E)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Ví đang hoạt động", color = Color.White) },
                                onClick = {
                                    showArchived = false
                                    menuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Ví đã ẩn", color = Color.White) },
                                onClick = {
                                    showArchived = true
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddWallet,
                containerColor = Color(0xFF4CAF50)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
        ) {
            // SCROLL CONTENT
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!showArchived) {
                    // TOTAL CARD
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                val currencyFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.forLanguageTag("vi-VN")))
                                Text(text = "Tổng tài sản", color = Color.Gray, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${currencyFormat.format(uiState.tongTaiSan.toLong())} đ",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = if (showArchived) "Danh sách ví ẩn" else "Danh sách ví",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(filteredWallets) { wallet ->
                    WalletItem(
                        wallet = wallet,
                        onClick = { onEditWallet(wallet.id) }
                    )
                }
            }
        }
    }
}
@Composable
fun WalletItem(

    wallet: WalletWithBalance,

    onClick: () -> Unit

) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(22.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2E)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            // ICON
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color = Color(
                            android.graphics.Color.parseColor(wallet.colorHex)
                        ),
                        shape = CircleShape
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = wallet.iconName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = wallet.name,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${wallet.transactionCount} giao dịch",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Row (
                horizontalArrangement = Arrangement.End
            ) {
                val currencyFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.forLanguageTag("vi-VN")))
                Text(
                    text = "${currencyFormat.format(wallet.soDuHienTai.toLong())}đ",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}