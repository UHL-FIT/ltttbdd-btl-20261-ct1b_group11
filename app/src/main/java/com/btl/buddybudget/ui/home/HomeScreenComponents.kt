package com.btl.buddybudget.ui.home

import androidx.compose.foundation.background
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

@Composable
fun MyWallet(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ví của tôi",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            walletrow(name = "Tiền mặt", amount = "1,000,000đ")
            Spacer(modifier = Modifier.height(16.dp))
            walletrow(name = "Ví tín dụng", amount = "1,000,000đ")
        }
    }
}

@Composable
fun walletrow(name: String, amount: String){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF333333), shape = RoundedCornerShape(20.dp)),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = name, color = Color.White, fontSize = 16.sp,
            modifier = Modifier.padding(start = 10.dp))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = amount, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}