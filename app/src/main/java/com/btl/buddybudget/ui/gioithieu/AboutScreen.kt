package com.btl.buddybudget.ui.gioithieu

import androidx.compose.foundation.clickable
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.btl.buddybudget.ui.home.Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.btl.buddybudget.R

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults

@Composable
fun AboutScreen(
    navController : NavController,
    viewModel: AboutViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val showImportConfirm by viewModel.showImportConfirm.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    // Hiển thị Toast thông báo
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Launcher để XUẤT file
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { viewModel.exportToJson(context, it) }
    }

    // Launcher để NHẬP file
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { viewModel.onImportFileSelected(it) }
    }

    // Dialog xác nhận khôi phục
    if (showImportConfirm) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissImportDialog() },
            title = { Text("Cảnh báo khôi phục") },
            text = { Text("Toàn bộ dữ liệu hiện tại trong máy sẽ bị xóa sạch và thay thế bằng dữ liệu từ file sao lưu này. Bạn có chắc chắn muốn tiếp tục?") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.confirmImport(context) }
                ) {
                    Text("Tiếp tục", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissImportDialog() }) {
                    Text("Hủy")
                }
            }
        )
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cài đặt",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 28.dp, bottom = 20.dp)
            )

            // --- CỤM 1: LOGO & TÊN APP ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(22.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "BudgetBuddy", color = MaterialTheme.colorScheme.onBackground, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Phiên bản 1.0", color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- CỤM 2: DANH SÁCH CHỨC NĂNG & CHUYỂN THEME ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // Dòng chuyển Theme
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Chế độ tối", color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onThemeChange(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.LightGray,
                            checkedIconColor = Color.White
                        )
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Screen.ManageCategory.route) }
                ) {
                    InfoRowTextOnly(title = "Danh Mục")
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Screen.Wallet.route) }
                ) {
                    InfoRowTextOnly(title = "Ví")
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

                // --- NÚT XUẤT DỮ LIỆU ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { exportLauncher.launch("buddy_budget_backup.json") }
                ) {
                    InfoRowTextOnly(title = "Xuất dữ liệu (JSON)")
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

                // --- NÚT NHẬP DỮ LIỆU ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { importLauncher.launch(arrayOf("application/json", "application/octet-stream")) }
                ) {
                    InfoRowTextOnly(title = "Nhập dữ liệu (JSON)")
                }
            }
            Spacer(modifier = Modifier.height(40.dp))

            // --- NÚT HƯỚNG DẪN SỬ DỤNG ---
            Button(
                onClick = { /* Xử lý hướng dẫn */ },
                modifier = Modifier
                    .height(46.dp)
                    .widthIn(min = 180.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Hướng dẫn sử dụng ›", color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun InfoRowTextOnly(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
    }
}
