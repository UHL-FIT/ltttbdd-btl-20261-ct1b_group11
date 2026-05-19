package com.btl.buddybudget.ui.home

sealed class Screen(val route: String) {
    object Overview : Screen("overview")
    object Statistics : Screen("statistics")
    object Budget : Screen("budget")
    object Profile : Screen("profile")
    object AddTransaction : Screen("add_transaction")
}