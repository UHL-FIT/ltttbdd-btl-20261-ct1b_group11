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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import com.btl.buddybudget.data.db.quanhe.ThongKeDanhMuc
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ThongKeScreen(
    viewModel: ThongKeViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBarSection(
            month = uiState.selectedMonth,
            year = uiState.selectedYear,
            onDateClick = { viewModel.toggleDatePicker(true) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TabSection(
            isExpenseSelected = uiState.isExpenseSelected,
            onTabSelected = { viewModel.onTabSelected(it) }
        )

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.displayItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Không có dữ liệu giao dịch", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            Spacer(modifier = Modifier.height(32.dp))

            // Vẽ biểu đồ dựa trên dữ liệu từ ViewModel
            PieChartSection(uiState.displayItems)

            Spacer(modifier = Modifier.height(32.dp))

            // Vẽ danh sách chi tiết
            ExpenseListSection(uiState.displayItems)
        }
    }

    if (uiState.isDatePickerVisible) {
        MonthYearPickerDialog(
            initialMonth = uiState.selectedMonth,
            initialYear = uiState.selectedYear,
            onDismiss = { viewModel.toggleDatePicker(false) },
            onConfirm = { month, year ->
                viewModel.onDateSelected(month, year)
            }
        )
    }
}

@Composable
fun TopBarSection(
    month: Int,
    year: Int,
    onDateClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp)
    ) {


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
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
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
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(25.dp))
            .padding(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        if (isExpenseSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
                    .clickable { onTabSelected(true) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Khoản chi",
                    color = if (isExpenseSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        if (!isExpenseSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
                    .clickable { onTabSelected(false) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Khoản thu",
                    color = if (!isExpenseSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun PieChartSection(items: List<ThongKeDisplayItem>) {
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
                val sweepAngle = item.sweepAngle

                drawArc(
                    color = Color(item.colorHex.toColorInt()),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = size
                )

                if (item.percentage > 0.05f) { // Chỉ hiện chữ nếu phần đủ lớn
                    val angleInDegrees = startAngle + sweepAngle / 2
                    val angleInRadians = Math.toRadians(angleInDegrees.toDouble())

                    val textRadius = radius * 0.6f
                    val x = center.x + textRadius * cos(angleInRadians).toFloat()
                    val y = center.y + textRadius * sin(angleInRadians).toFloat()

                    val text = item.percentageText
                    val textLayoutResult = textMeasurer.measure(
                        text = text,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 12.sp,
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
                }
                startAngle += sweepAngle
            }
        }
    }
}

@Composable
fun ExpenseListSection(items: List<ThongKeDisplayItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        itemsIndexed(items) { index, item ->
            ExpenseListItem(item)

            if (index < items.lastIndex) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(start = 72.dp)
                )
            }
        }
    }
}

@Composable
fun ExpenseListItem(item: ThongKeDisplayItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = Color(item.colorHex.toColorInt()),
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = item.iconName, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
            Text(
                text = item.percentageText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }

        Text(
            text = item.formattedTotal,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MonthYearPickerDialog(
    initialMonth: Int,
    initialYear: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var month by remember { mutableIntStateOf(initialMonth) }
    var year by remember { mutableIntStateOf(initialYear) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Chọn thời gian",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    IconButton(onClick = { year-- }) {
                        Icon(Icons.Default.KeyboardArrowLeft, null)
                    }
                    Text(
                        text = "$year",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { year++ }) {
                        Icon(Icons.Default.KeyboardArrowRight, null)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                                        .background(if (isSelected) MaterialTheme.colorScheme.onSurfaceVariant else Color.Transparent)
                                        .clickable { month = m }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Tháng $m",
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
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
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = { onConfirm(month, year) }) {
                        Text("Chọn")
                    }
                }
            }
        }
    }
}
