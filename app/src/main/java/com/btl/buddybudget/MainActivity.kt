package com.btl.buddybudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.btl.buddybudget.ui.home.HomeScreen
import com.btl.buddybudget.ui.theme.BuddyBudgetTheme



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.isSystemInDarkTheme

import android.util.Log

import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivityLifeCycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Activity đang được khởi tạo")
        enableEdgeToEdge()

        // Ẩn thanh điều hướng (Navigation Bar)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())

        val app = application as BuddyBudgetApplication
        val appContainer = app.container
        val userSettings = appContainer.userSettings

        setContent {
            // Quan sát chế độ tối sáng từ DataStore
            val isDarkSetting by userSettings.isDarkMode.collectAsState(initial = true)
            val darkTheme = isDarkSetting ?: isSystemInDarkTheme()

            BuddyBudgetTheme(darkTheme = darkTheme) {
                HomeScreen(
                    viewModelFactory = appContainer.viewModelFactory,
                    isDarkTheme = darkTheme,
                    onThemeChange = { isDark ->
                        lifecycleScope.launch {
                            userSettings.setDarkMode(isDark)
                        }
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Activity bắt đầu hiển thị")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Activity đã sẵn sàng tương tác")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Activity tạm dừng")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity không còn hiển thị")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity bị hủy")
    }
}
