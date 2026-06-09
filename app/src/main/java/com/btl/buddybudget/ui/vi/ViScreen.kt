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
import androidx.compose.ui.draw.clip
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

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
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

        // --- NÚT MENU HÌNH TRÒN ---
        Box(
            modifier = Modifier
                .padding(end = 20.dp, top = 20.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { menuExpanded = true }
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            DropdownMenu(
                shape = RoundedCornerShape(24.dp),
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                DropdownMenuItem(
                    text = { Text("Ví đang hoạt động", color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        showArchived = false
                        menuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Ví đã ẩn", color = MaterialTheme.colorScheme.onSurface) },
                    onClick = {
                        showArchived = true
                        menuExpanded = false
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiêu đề căn giữa
            Text(
                text = if (showArchived) "Ví đã ẩn" else "Quản lý ví",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 28.dp, bottom = 20.dp)
            )

            // SCROLL CONTENT
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (!showArchived) {
                    // TOTAL CARD
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                val currencyFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.forLanguageTag("vi-VN")))
                                Text(text = "Tổng tài sản", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${currencyFormat.format(uiState.tongTaiSan.toLong())} đ",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Nút Thêm ví mới (Capsule style)
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(28.dp))
                                .clickable { onAddWallet() }
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.primary)
                            Text("  Thêm ví mới", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item {
                    Text(
                        text = if (showArchived) "Danh sách ví ẩn" else "Danh sách ví",
                        color = MaterialTheme.colorScheme.onBackground,
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
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
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
                        color = Color(android.graphics.Color.parseColor(wallet.colorHex)),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = wallet.iconName,
                    color = Color.White, // Icon emoji nên giữ màu trắng/mặc định
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
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${wallet.transactionCount} giao dịch",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }

            Row (
                horizontalArrangement = Arrangement.End
            ) {
                val currencyFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.forLanguageTag("vi-VN")))
                Text(
                    text = "${currencyFormat.format(wallet.soDuHienTai.toLong())}đ",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
