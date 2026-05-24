package com.btl.buddybudget.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import com.btl.buddybudget.data.db.quanhe.WalletWithBalance
import com.btl.buddybudget.ui.vi.ViScreenState

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun MyWallet(
    uiState: ViScreenState,
    onViewAllClick: () -> Unit,
    onTransactionHistoryClick: () -> Unit
) {
    val formatter = remember {
        val symbols = DecimalFormatSymbols(Locale.forLanguageTag("vi-VN"))
        DecimalFormat("#,###", symbols)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Tổng số dư",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "${formatter.format(uiState.tongTaiSan.toLong())} đ",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Xem tất cả",
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onViewAllClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            uiState.wallets.take(3).forEachIndexed { index, wallet ->
                WalletRow(wallet = wallet)
                if (index < uiState.wallets.take(3).size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 0.5.dp,
                        color = Color.Gray.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
fun WalletRow(wallet: WalletWithBalance) {
    val formatter = remember {
        val symbols = DecimalFormatSymbols(Locale.forLanguageTag("vi-VN"))
        DecimalFormat("#,###", symbols)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    color = Color(android.graphics.Color.parseColor(wallet.colorHex)).copy(alpha = 0.2f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = wallet.iconName,
                color = Color(android.graphics.Color.parseColor(wallet.colorHex)),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = wallet.name,
            color = Color.White,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${formatter.format(wallet.soDuHienTai.toLong())} đ",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
