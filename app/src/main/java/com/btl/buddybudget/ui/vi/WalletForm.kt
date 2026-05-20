package com.btl.buddybudget.ui.vi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WalletForm(

    state: ViUIState,

    onNameChange: (String) -> Unit,

    onAmountChange: (String) -> Unit,

    onCurrencyChange: (String) -> Unit

) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = {
                    Text("Tên ví")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.soDu,
                onValueChange = onAmountChange,
                label = {
                    Text("Số dư ban đầu")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.donVi,
                onValueChange = onCurrencyChange,
                label = {
                    Text("Đơn vị tiền")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}