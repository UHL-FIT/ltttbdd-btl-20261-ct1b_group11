package com.btl.buddybudget.ui.thongke

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin

// Định nghĩa Data class
data class ExpenseItem(
    val id: Int,
    val name: String,
    val icon: String,
    val percentage: Float,
    val amount: String,
    val color: Color
)

// ------------------------------------------------------------------
// --- 1. ĐÃ CẬP NHẬT DỮ LIỆU MẪU KHOẢN CHI & MÀU SẮC THEO ẢNH ---
// ------------------------------------------------------------------
val expenseList = listOf(
    ExpenseItem(1, "Du lịch", "✈️", 10.0f, "100.000đ", Color(0xFFFF5252)),     // Đỏ (Vibrant Red)
    ExpenseItem(2, "Quà tặng", "🎁", 20.0f, "200.000đ", Color(0xFF9B51E0)),    // Tím (Purple)
    ExpenseItem(3, "Mua sắm", "👗", 15.0f, "150.000đ", Color(0xFF2F80ED)),     // Xanh dương đậm (Dark Blue)
    ExpenseItem(4, "Thể thao", "⚽", 15.0f, "150.000đ", Color(0xFF00ACC1)),     // Xanh ngọc (Cyan)
    ExpenseItem(5, "Sức khỏe", "👩‍⚕️", 15.0f, "150.000đ", Color(0xFF27AE60)),     // Xanh lá (Green)
    ExpenseItem(6, "Hóa đơn", "💡", 15.0f, "150.000đ", Color(0xFFFBC02D)),      // Vàng (Yellow)
    ExpenseItem(7, "Ăn uống", "🍜", 10.0f, "100.000đ", Color(0xFFD35400))       // Cam (Orange)
    // TODO: Phần trăm hiện tại đang được gán cứng cho tổng bằng 100%, sau này bạn sẽ handling logic này sau
)

// --- 2. CẬP NHẬT LUÔN DỮ LIỆU KHOẢN THU THEO BẢNG MÀU MỚI (CHỌN MỘT SỐ MÀU CHÍNH) ---
val incomeList = listOf(
    ExpenseItem(1, "Lương", "💰", 70.0f, "15.000.000đ", Color(0xFF27AE60)),     // Xanh lá (Green) - Màu chính cho Thu
    ExpenseItem(2, "Thưởng", "🎁", 25.0f, "5.000.000đ", Color(0xFFFBC02D)),     // Vàng (Yellow)
    ExpenseItem(3, "Khác", "📦", 5.0f, "1.000.000đ", Color(0xFF00ACC1))        // Xanh ngọc (Cyan)
)

// Màu nền dark-mode
val AppBlack = Color(0xFF000000)
val DarkSurface = Color(0xFF1C1C1E)
val TextGray = Color(0xFFAAAAAA)

@Composable
fun ThongKeScreen(onBack: () -> Unit) {
    // Trạng thái Tab (Thu/Chi)
    var isExpenseSelected by remember { mutableStateOf(true) }

    // Trạng thái Thời gian hiện tại
    val calendar = Calendar.getInstance()
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var showDatePicker by remember { mutableStateOf(false) }

    val currentDataList = if (isExpenseSelected) expenseList else incomeList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBlack)
    ) {
        TopBarSection(
            month = selectedMonth,
            year = selectedYear,
            onDateClick = { showDatePicker = true },
            onBack = { onBack() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TabSection(
            isExpenseSelected = isExpenseSelected,
            onTabSelected = { selected ->
                isExpenseSelected = selected
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Vẽ biểu đồ dựa trên dữ liệu hiện tại (Thu hoặc Chi)
        PieChartSection(currentDataList)

        Spacer(modifier = Modifier.height(32.dp))

        // Vẽ danh sách dựa trên dữ liệu hiện tại (Thu hoặc Chi), danh sách này đã có hiển thị %
        ExpenseListSection(currentDataList)
    }

    if (showDatePicker) {
        MonthYearPickerDialog(
            initialMonth = selectedMonth,
            initialYear = selectedYear,
            onDismiss = { showDatePicker = false },
            onConfirm = { month, year ->
                selectedMonth = month
                selectedYear = year
                showDatePicker = false

                // TODO: Gọi ViewModel ở đây để load lại dữ liệu theo tháng/năm mới
            }
        )
    }
}

// ---------------- CÁC THÀNH PHẦN GIAO DIỆN CON ----------------

@Composable
fun TopBarSection(
    month: Int,
    year: Int,
    onDateClick: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF1C1C1E))
                .clickable { onBack() }
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "‹",
                color = Color(0xFF0A84FF),
                fontSize = 28.sp,
                modifier = Modifier.offset(y = (-2).dp)
            )
        }

        // Biến thành nút bấm chọn ngày "Tháng X, YYYY ▼"
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onDateClick() }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tháng $month, $year",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Mở chọn tháng",
                tint = Color.White
            )
        }
    }
}

@Composable
fun MonthYearPickerDialog(
    initialMonth: Int,
    initialYear: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var month by remember { mutableStateOf(initialMonth) }
    var year by remember { mutableStateOf(initialYear) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF2C2C2E) // Nền xám đen
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Chọn thời gian", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Năm trước",
                        tint = Color.White,
                        modifier = Modifier.clickable { year-- }
                    )
                    Text(text = "$year", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Năm sau",
                        tint = Color.White,
                        modifier = Modifier.clickable { year++ }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Lưới chọn Tháng (3 cột x 4 hàng)
                val months = (1..12).toList()
                Column(modifier = Modifier.fillMaxWidth()) {
                    for (row in 0..3) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (col in 0..2) {
                                val m = months[row * 3 + col]
                                val isSelected = m == month
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Color(0xFF4CAF50) else Color.Transparent)
                                        .clickable { month = m }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Tháng $m",
                                        color = if (isSelected) Color.White else Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy", color = Color.Gray, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = { onConfirm(month, year) }) {
                        Text("Chọn", color = Color(0xFF4CAF50), fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TabSection(
    isExpenseSelected: Boolean,
    onTabSelected: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .height(50.dp)
            .background(DarkSurface, RoundedCornerShape(25.dp))
            .padding(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (isExpenseSelected) Color(0xFF4A4A4C) else Color.Transparent,
                        RoundedCornerShape(25.dp)
                    )
                    .clickable { onTabSelected(true) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Khoản chi",
                    color = Color.White,
                    fontWeight = if (isExpenseSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 15.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (!isExpenseSelected) Color(0xFF4A4A4C) else Color.Transparent,
                        RoundedCornerShape(25.dp)
                    )
                    .clickable { onTabSelected(false) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Khoản thu",
                    color = Color.White,
                    fontWeight = if (!isExpenseSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 15.sp
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------------
// --- 3. ĐÃ CẬP NHẬT LOGIC VẼ CHỮ `%` TRONG BIỂU ĐỒ (LUÔN DÙNG CHỮ TRẮNG) ---
// -----------------------------------------------------------------------------------
@Composable
fun PieChartSection(items: List<ExpenseItem>) {
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            var startAngle = -90f
            val radius = size.minDimension / 2
            val center = Offset(size.width / 2, size.height / 2)

            items.forEach { item ->
                val sweepAngle = (item.percentage / 100f) * 360f

                drawArc(
                    color = item.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = size
                )

                val angleInDegrees = startAngle + sweepAngle / 2
                val angleInRadians = Math.toRadians(angleInDegrees.toDouble())

                val textRadius = radius * 0.6f
                val x = center.x + textRadius * cos(angleInRadians).toFloat()
                val y = center.y + textRadius * sin(angleInRadians).toFloat()

                // Hiển thị phần trăm làm tròn
                val text = "${item.percentage.toInt()}%"

                val textLayoutResult = textMeasurer.measure(
                    text = text,
                    style = TextStyle(
                        // THEO ẢNH MẪU: Chữ phần trăm luôn màu Trắng để nổi bật trên nền sậm
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                        x = x - textLayoutResult.size.width / 2,
                        y = y - textLayoutResult.size.height / 2
                    )
                )

                startAngle += sweepAngle
            }
        }
    }
}

@Composable
fun ExpenseListSection(items: List<ExpenseItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
    ) {
        itemsIndexed(items) { index, item ->
            ExpenseListItem(item)

            if (index < items.lastIndex) {
                Divider(
                    color = Color(0xFF38383A),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(start = 72.dp)
                )
            }
        }
    }
}

// GIAO DIỆN MỤC DANH SÁCH (ĐÃ CÓ PHẦN TRĂM DƯỚI TÊN)
@Composable
fun ExpenseListItem(item: ExpenseItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = item.color,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = item.icon, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.name,
                color = Color.White,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Hiển thị phần trăm ở đây
            Text(
                text = "${item.percentage}%",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }

        Text(
            text = item.amount,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}