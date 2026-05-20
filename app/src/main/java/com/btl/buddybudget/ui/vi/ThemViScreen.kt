package com.btl.buddybudget.ui.vi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.btl.buddybudget.ui.theme.BuddyBudgetTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemViScreen(
    onBack: () -> Unit,
    viewModel: ThemViViewModel = viewModel()
) {

    val state = viewModel.uiState

    Scaffold(
        containerColor = Color.Black,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Thêm ví",
                        color = Color.White
                    )
                },

                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            null,
                            tint = Color.White
                        )
                    }
                },

                actions = {
                    TextButton(
                        onClick = {
                            viewModel.taoVi()
                        }
                    ) {
                        Text(
                            "LƯU",
                            color = Color(0xFF4CAF50)
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
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
                        onValueChange = viewModel::doiTenVi,
                        label = {
                            Text("Tên ví")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.soDu,
                        onValueChange = viewModel::doiSoDu,
                        label = {
                            Text("Số dư ban đầu")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.donVi,
                        onValueChange = viewModel::doiDonVi,
                        label = {
                            Text("Đơn vị tiền")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1E1E)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
/*
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            "Không tính vào tổng",
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = !state.includeInTotal,
                            onCheckedChange = {
                                viewModel.onIncludeTotalChange(!it)
                            }
                        )
                    }

 */

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            "Lưu trữ ví",
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = state.isArchived,
                            onCheckedChange = viewModel::anVi
                        )
                    }
                }
            }

            state.error?.let {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }
        }
    }
}
