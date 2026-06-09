package com.btl.buddybudget.ui.giaodich
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ThemGiaoDichScreen(
    viewModel: ThemGiaoDichViewModel,
    onCancel: () -> Unit,
    onNavigateToSelectGroup: (Boolean) -> Unit,
    onNavigateToSelectWallet: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearErrorMessage()
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.date
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onDateChanged(it)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Hủy")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Thêm giao dịch",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 28.dp, bottom = 20.dp)
            )

            // --- KHỐI NHẬP LIỆU CHÍNH ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                // 1. Khoản chi / Khoản thu
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (uiState.idExpense) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            .clickable { viewModel.onTransactionTypeChanged(true) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Khoản chi",
                            color = if (uiState.idExpense) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (!uiState.idExpense) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                            .clickable { viewModel.onTransactionTypeChanged(false) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Khoản thu",
                            color = if (!uiState.idExpense) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Chọn ví
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToSelectWallet() }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(uiState.selectedWalletIcon, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = uiState.selectedWalletName, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                    }
                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(vertical = 8.dp))

                // 3. Số tiền
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text("Số tiền", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Text("VND", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                        BasicTextField(
                            value = uiState.amount,
                            onValueChange = { viewModel.onAmountChanged(it) },
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 28.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            decorationBox = { innerTextField ->
                                if (uiState.amount.isEmpty()) {
                                    Text("0", color = MaterialTheme.colorScheme.onSurface, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(vertical = 8.dp))

                // 4. Chọn nhóm
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToSelectGroup(uiState.idExpense) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = try { Color(android.graphics.Color.parseColor(uiState.selectedGroupColor)).copy(alpha = 0.2f) } catch(e: Exception) { MaterialTheme.colorScheme.surfaceVariant },
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = uiState.selectedGroupIcon, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = uiState.selectedGroupName,
                            color = if (uiState.selectedGroupName == "Chọn nhóm") MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                        )
                    }
                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(vertical = 8.dp))

                // 5. Ghi chú
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    BasicTextField(
                        value = uiState.note,
                        onValueChange = { viewModel.onNoteChanged(it) },
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        decorationBox = { innerTextField ->
                            if (uiState.note.isEmpty()) {
                                Text("Ghi chú", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp)
                            }
                            innerTextField()
                        }
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.padding(vertical = 8.dp))

                // 6. Ngày giao dịch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        val locale = Locale.forLanguageTag("vi-VN")
                        val dateFormatter = remember(locale) { SimpleDateFormat("dd/MM/yyyy", locale) }
                        Text(text = dateFormatter.format(Date(uiState.date)), color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                    }
                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Khoảng trống linh hoạt để đẩy nút Lưu xuống dưới cùng
            Spacer(modifier = Modifier.weight(1f))

            // --- NÚT LƯU CỐ ĐỊNH PHÍA DƯỚI ---
            Button(
                onClick = { viewModel.saveTransaction(onSuccess = { onCancel() }) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Lưu", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        // --- NÚT QUAY VỀ HÌNH TRÒN ---
        Box(
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable { 
                    viewModel.resetForm()
                    onCancel() 
                }
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


        // Snackbar hiển thị cảnh báo (Đặt trong Box để align đúng)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp)
        )
    }
}
